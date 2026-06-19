import os
import json
from datetime import datetime
from qrcode import make

LINEAS = [
    ("1A", "Av. Diego de Almagro / 5to Centenario"),
    ("1B", "ULA / 5to Centenario"),
    ("1C", "Tec. ULA / 5to Centenario"),
    ("1D", "ULA / 5to Centenario"),
    ("2A", "ULA / 5to Centenario"),
    ("2B", "Tec. ULA / 5to Centenario"),
    ("3", "Tec. ULA / Rahue"),
    ("3A", "Tec. ULA / Rahue"),
    ("4", "ULA / Rahue"),
    ("5", "Los Dominicos / Ovejería"),
    ("5A", "ULA / Ovejería"),
    ("7", "Francke / Kolbe"),
    ("7A", "Hospital - Francke / Kolbe"),
    ("7B", "ULA - Francke / Kolbe"),
]

CARGAS = [1000, 2000, 5000, 10000]

def generar_qr():
    os.makedirs("qr_osorno", exist_ok=True)
    hora = datetime.now().strftime("%H:%M")

    for linea, recorrido in LINEAS:
        data = json.dumps({"linea": linea, "hora": hora})
        img = make(data)
        nombre_archivo = f"qr_osorno/linea_{linea.replace(' ', '_')}.png"
        img.save(nombre_archivo)
        print(f"[OK] {linea} - {recorrido} -> {nombre_archivo}")

    print(f"\nGenerados {len(LINEAS)} QR de líneas")

    for monto in CARGAS:
        data = json.dumps({"tipo": "carga", "monto": monto})
        img = make(data)
        nombre_archivo = f"qr_osorno/carga_${monto}.png"
        img.save(nombre_archivo)
        print(f"[OK] Carga ${monto} -> {nombre_archivo}")

    print(f"\nGenerados {len(CARGAS)} QR de carga")
    print(f"\nTotal: {len(LINEAS) + len(CARGAS)} QR en carpeta 'qr_osorno/'")

if __name__ == "__main__":
    generar_qr()
