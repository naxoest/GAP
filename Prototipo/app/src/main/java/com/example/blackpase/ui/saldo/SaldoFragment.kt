package com.example.blackpase.ui.saldo

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.example.blackpase.model.TipoCliente
import com.example.blackpase.ui.historial.HistorialAdapter
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SaldoFragment : Fragment() {

    private lateinit var saldoViewModel: SaldoViewModel
    private lateinit var historialAdapter: HistorialAdapter
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null
    private var isProcessing = false
    private var currentDialog: AlertDialog? = null
    private var currentPreview: PreviewView? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mostrarDialogoCarga()
        } else {
            Toast.makeText(requireContext(), "Se requiere permiso de cámara", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saldo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saldoViewModel = ViewModelProvider(this).get(SaldoViewModel::class.java)

        val tvSaldo = view.findViewById<TextView>(R.id.tvSaldo)
        val tvTarifaActual = view.findViewById<TextView>(R.id.tvTarifaActual)
        val spinnerTipoCliente = view.findViewById<Spinner>(R.id.spinnerTipoCliente)
        val rvUltimosViajes = view.findViewById<RecyclerView>(R.id.rvUltimosViajes)
        val btnCargarSaldo = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCargarSaldo)

        cameraExecutor = Executors.newSingleThreadExecutor()

        historialAdapter = HistorialAdapter()
        rvUltimosViajes.layoutManager = LinearLayoutManager(requireContext())
        rvUltimosViajes.adapter = historialAdapter

        val tiposCliente = TipoCliente.values()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tiposCliente.map { it.displayName }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoCliente.adapter = adapter

        spinnerTipoCliente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                saldoViewModel.actualizarTipoCliente(tiposCliente[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        saldoViewModel.saldo.observe(viewLifecycleOwner) { saldo ->
            tvSaldo.text = "$${String.format("%,d", saldo).replace(",", ".")}"
        }

        saldoViewModel.tarifa.observe(viewLifecycleOwner) { tarifa ->
            tvTarifaActual.text = "Tarifa: $${tarifa}"
        }

        saldoViewModel.ultimosViajes.observe(viewLifecycleOwner) { viajes ->
            historialAdapter.submitList(viajes)
        }

        btnCargarSaldo.setOnClickListener {
            verificarPermisoCamara()
        }

        crearCanalNotificacion()
    }

    override fun onResume() {
        super.onResume()
        saldoViewModel.cargarDatos()
    }

    private fun verificarPermisoCamara() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                mostrarDialogoCarga()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(), "Se requiere permiso de cámara para escanear QR", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun mostrarDialogoCarga() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_cargar_saldo, null)
        val cameraPreview = dialogView.findViewById<PreviewView>(R.id.previewViewCarga)
        currentPreview = cameraPreview

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Cargar Saldo")
            .setView(dialogView)
            .setNegativeButton("Cancelar") { _, _ -> }
            .create()

        currentDialog = dialog
        dialog.show()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPreview.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        if (!isProcessing) {
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                escanearQR(image, dialog)
                            }
                        }
                        imageProxy.close()
                    }
                }

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al iniciar cámara", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(requireContext()))

        dialog.setOnDismissListener {
            try { cameraProvider?.unbindAll() } catch (_: Exception) {}
        }
    }

    private fun reiniciarCamara() {
        val provider = cameraProvider ?: return
        val preview = currentPreview ?: return
        val dialog = currentDialog ?: return
        try {
            provider.unbindAll()

            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(preview.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        if (!isProcessing) {
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                escanearQR(image, dialog)
                            }
                        }
                        imageProxy.close()
                    }
                }

            provider.bindToLifecycle(
                viewLifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                imageAnalysis
            )
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al reiniciar cámara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun escanearQR(image: InputImage, dialog: AlertDialog) {
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    if (barcode.valueType == Barcode.TYPE_TEXT) {
                        val qrData = barcode.rawValue
                        qrData?.let {
                            isProcessing = true
                            procesarCarga(it, dialog)
                        }
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener {
                // QR no detectado, continuar escaneando
            }
    }

    private fun procesarCarga(qrData: String, dialog: AlertDialog) {
        val gson = com.google.gson.Gson()

        val linea = try {
            gson.fromJson(qrData, com.example.blackpase.model.QRRuta::class.java)
        } catch (e: Exception) {
            null
        }

        if (linea != null && linea.linea.isNotBlank()) {
            try { cameraProvider?.unbindAll() } catch (_: Exception) {}
            try {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("QR Incorrecto")
                    .setMessage("Este QR es de línea de micro.\nVe a la pestaña Pagar para viajar.")
                    .setPositiveButton("Volver a escanear") { _, _ ->
                        isProcessing = false
                        reiniciarCamara()
                    }
                    .setCancelable(false)
                    .create()
                dialog.show()
            } catch (e: Exception) {
                isProcessing = false
                Toast.makeText(requireContext(), "Este QR es de línea de micro. Ve a Pagar.", Toast.LENGTH_LONG).show()
            }
            return
        }

        try {
            val carga = gson.fromJson(qrData, com.example.blackpase.model.QRCarga::class.java)

            if (carga.tipo == "carga" && carga.monto > 0) {
                dialog.dismiss()
                mostrarConfirmacionCarga(carga.monto)
            } else {
                isProcessing = false
                Toast.makeText(requireContext(), "Código QR de carga inválido", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            isProcessing = false
            Toast.makeText(requireContext(), "Código QR inválido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarConfirmacionCarga(monto: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirmar_carga, null)
        val tvMonto = dialogView.findViewById<TextView>(R.id.tvMontoCarga)
        val tvSaldoActual = dialogView.findViewById<TextView>(R.id.tvSaldoActualCarga)
        val tvSaldoFinal = dialogView.findViewById<TextView>(R.id.tvSaldoFinalCarga)

        tvMonto.text = "+$$monto"
        tvSaldoActual.text = "Saldo actual: $${MockData.saldoActual}"
        tvSaldoFinal.text = "Saldo después: $${MockData.saldoActual + monto}"

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Carga")
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancelarCarga).setOnClickListener {
            dialog.dismiss()
            isProcessing = false
        }

        dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnConfirmarCarga).setOnClickListener {
            dialog.dismiss()
            val nuevoSaldo = MockData.saldoActual + monto
            MockData.saldoActual = nuevoSaldo

            val transaccion = com.example.blackpase.model.Transaccion(
                id = MockData.generarId(),
                fecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                hora = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                tipo = com.example.blackpase.model.TipoTransaccion.CARGA,
                monto = monto,
                saldoRestante = nuevoSaldo
            )
            MockData.agregarTransaccion(transaccion)

            saldoViewModel.cargarDatos()
            mostrarNotificacionCarga(monto)
            Toast.makeText(requireContext(), "Saldo cargado: $${monto}", Toast.LENGTH_LONG).show()
            isProcessing = false
        }

        dialog.show()
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombre = "Cargas de Saldo"
            val descripcion = "Notificaciones de cargas realizadas"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel("cargas_channel", nombre, importancia).apply {
                description = descripcion
            }
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    private fun mostrarNotificacionCarga(monto: Int) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            return
        }

        val notificacion = NotificationCompat.Builder(requireContext(), "cargas_channel")
            .setSmallIcon(R.drawable.ic_wallet)
            .setContentTitle("Saldo Cargado")
            .setContentText("Se cargaron $$monto a tu cuenta")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Se cargaron $$monto a tu cuenta\nSaldo actual: $${MockData.saldoActual}"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(requireContext()).notify(2, notificacion)
    }
}
