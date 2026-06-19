package com.example.blackpase.ui.pagar

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
import android.widget.ImageView
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
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PagarFragment : Fragment() {

    private lateinit var pagarViewModel: PagarViewModel
    private lateinit var cameraPreview: PreviewView
    private lateinit var tvCategoria: TextView
    private lateinit var tvSaldoActual: TextView
    private lateinit var cameraExecutor: ExecutorService
    private var isProcessing = false
    private var cameraProvider: ProcessCameraProvider? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Se requiere permiso de cámara", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pagar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagarViewModel = ViewModelProvider(this).get(PagarViewModel::class.java)

        cameraPreview = view.findViewById(R.id.previewView)
        tvCategoria = view.findViewById(R.id.tvCategoria)
        tvSaldoActual = view.findViewById(R.id.tvSaldoActual)

        cameraExecutor = Executors.newSingleThreadExecutor()

        crearCanalNotificacion()

        pagarViewModel.saldoActual.observe(viewLifecycleOwner) { saldo ->
            tvSaldoActual.text = "Saldo actual: $${String.format("%,d", saldo).replace(",", ".")}"
        }

        pagarViewModel.tipoCliente.observe(viewLifecycleOwner) { tipo ->
            tvCategoria.text = "${tipo.displayName} ($${tipo.tarifa})"
        }

        pagarViewModel.mensajeError.observe(viewLifecycleOwner) { error ->
            isProcessing = false
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }

        verificarPermisoCamara()
    }

    override fun onResume() {
        super.onResume()
        pagarViewModel.cargarDatos()
    }

    private fun verificarPermisoCamara() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
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

    private fun startCamera() {
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
                                escanearQR(image)
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
    }

    private fun detenerCamara() {
        cameraProvider?.unbindAll()
    }

    private fun reiniciarCamara() {
        val provider = cameraProvider ?: return
        try {
            provider.unbindAll()

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
                                escanearQR(image)
                            }
                        }
                        imageProxy.close()
                    }
                }

            provider.bindToLifecycle(
                viewLifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al reiniciar cámara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun escanearQR(image: InputImage) {
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    if (barcode.valueType == Barcode.TYPE_TEXT) {
                        val qrData = barcode.rawValue
                        qrData?.let {
                            isProcessing = true
                            detenerCamara()
                            mostrarConfirmacion(it)
                        }
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener {
                // QR no detectado, continuar escaneando
            }
    }

    private fun mostrarConfirmacion(qrData: String) {
        val gson = com.google.gson.Gson()

        val carga = try {
            gson.fromJson(qrData, com.example.blackpase.model.QRCarga::class.java)
        } catch (e: Exception) {
            null
        }

        if (carga != null && carga.tipo == "carga") {
            try { cameraProvider?.unbindAll() } catch (_: Exception) {}
            try {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("QR Incorrecto")
                    .setMessage("Este QR es de carga de saldo.\nVe a la pestaña Saldo para cargar.")
                    .setPositiveButton("Volver a escanear") { _, _ ->
                        isProcessing = false
                        reiniciarCamara()
                    }
                    .setCancelable(false)
                    .create()
                dialog.show()
            } catch (e: Exception) {
                isProcessing = false
                Toast.makeText(requireContext(), "Este QR es de carga de saldo. Ve a Saldo.", Toast.LENGTH_LONG).show()
                reiniciarCamara()
            }
            return
        }

        val qrRuta = try {
            gson.fromJson(qrData, com.example.blackpase.model.QRRuta::class.java)
        } catch (e: Exception) {
            null
        }

        if (qrRuta == null || qrRuta.linea.isNullOrBlank()) {
            isProcessing = false
            startCamera()
            Toast.makeText(requireContext(), "Código QR inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val nombreLinea = MockData.getNombreLinea(qrRuta.linea)
        val tarifa = MockData.tipoClienteActual.tarifa
        val tieneSaldo = MockData.saldoActual >= tarifa

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirmar_pago, null)
        val tvLinea = dialogView.findViewById<TextView>(R.id.tvLineaDialog)
        val tvNombreLinea = dialogView.findViewById<TextView>(R.id.tvNombreLineaDialog)
        val tvTarifa = dialogView.findViewById<TextView>(R.id.tvTarifaDialog)
        val tvSinSaldo = dialogView.findViewById<TextView>(R.id.tvSinSaldo)
        val btnCancelar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnConfirmarPago)
        val btnIrSaldo = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnIrSaldo)

        tvLinea.text = "Línea ${qrRuta.linea}"
        tvNombreLinea.text = nombreLinea
        tvTarifa.text = "$$tarifa"

        val dialog = AlertDialog.Builder(requireContext(), R.style.ConfirmDialogTheme)
            .setTitle("Confirmar Pago")
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnCancelar.setOnClickListener {
            dialog.dismiss()
            isProcessing = false
            startCamera()
        }

        if (tieneSaldo) {
            btnConfirmar.setOnClickListener {
                dialog.dismiss()
                pagarViewModel.procesarQR(qrData)
                mostrarPagoExitoso(qrRuta.linea, nombreLinea, tarifa)
            }
        } else {
            btnConfirmar.visibility = View.GONE
            tvSinSaldo.visibility = View.VISIBLE
            btnIrSaldo.visibility = View.VISIBLE

            btnIrSaldo.setOnClickListener {
                dialog.dismiss()
                isProcessing = false
                val navController = androidx.navigation.Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                navController.navigate(R.id.navigation_saldo)
            }
        }

        dialog.show()
    }

    private fun mostrarPagoExitoso(linea: String, nombreLinea: String, tarifa: Int) {
        val saldoRestante = MockData.saldoActual - tarifa

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pago_exitoso, null)
        val tvLinea = dialogView.findViewById<TextView>(R.id.tvLineaDialogExito)
        val tvNombreLinea = dialogView.findViewById<TextView>(R.id.tvNombreLineaDialogExito)
        val tvTarifa = dialogView.findViewById<TextView>(R.id.tvTarifaDialogExito)
        val btnVolver = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnVolverEscanear)
        val btnIrSaldo = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnIrSaldo)

        tvLinea.text = "Línea $linea"
        tvNombreLinea.text = nombreLinea
        tvTarifa.text = "-$$tarifa"

        val dialog = AlertDialog.Builder(requireContext(), R.style.ConfirmDialogTheme)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnVolver.setOnClickListener {
            dialog.dismiss()
            mostrarEncuestaCes(linea)
        }

        btnIrSaldo.setOnClickListener {
            dialog.dismiss()
            isProcessing = false
            val navController = androidx.navigation.Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.navigation_saldo)
        }

        dialog.show()
        mostrarNotificacionPago(linea, nombreLinea, tarifa, saldoRestante)
    }

    private fun mostrarEncuestaCes(linea: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_encuesta_ces, null)

        val ratingBar = dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarCes)
        val tvPuntuacion = dialogView.findViewById<TextView>(R.id.tvPuntuacion)
        val etComentario = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etComentario)
        val btnOmitir = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnOmitir)
        val btnEnviar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnEnviarCes)

        val textosPuntuacion = arrayOf("", "Muy difícil", "Difícil", "Neutral", "Fácil", "Muy fácil")

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            tvPuntuacion.text = textosPuntuacion[rating.toInt()]
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnOmitir.setOnClickListener {
            dialog.dismiss()
            isProcessing = false
            startCamera()
        }

        btnEnviar.setOnClickListener {
            val puntuacion = ratingBar.rating.toInt()
            if (puntuacion == 0) {
                Toast.makeText(requireContext(), "Selecciona una puntuación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val feedback = com.example.blackpase.model.FeedbackCes(
                id = MockData.generarId(),
                fecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                hora = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                puntuacion = puntuacion,
                comentario = etComentario.text.toString(),
                tipoCliente = MockData.tipoClienteActual.displayName,
                linea = linea
            )
            MockData.agregarFeedback(feedback)

            dialog.dismiss()
            Toast.makeText(requireContext(), "¡Gracias por tu feedback!", Toast.LENGTH_SHORT).show()
            isProcessing = false
            startCamera()
        }

        dialog.show()
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombre = "Pagos de Transporte"
            val descripcion = "Notificaciones de pagos realizados"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel("pagos_channel", nombre, importancia).apply {
                description = descripcion
            }
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    private fun mostrarNotificacionPago(linea: String, nombreLinea: String, tarifa: Int, saldoRestante: Int) {
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

        val notificacion = NotificationCompat.Builder(requireContext(), "pagos_channel")
            .setSmallIcon(R.drawable.ic_directions_bus)
            .setContentTitle("Viaje Registrado")
            .setContentText("Línea $linea - $nombreLinea")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Línea $linea - $nombreLinea\nTarifa: $$tarifa\nSaldo restante: $$saldoRestante"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(requireContext()).notify(1, notificacion)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }
}
