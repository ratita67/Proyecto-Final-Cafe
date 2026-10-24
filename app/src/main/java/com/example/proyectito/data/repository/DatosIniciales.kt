package com.example.proyectito.data.repository

import com.example.proyectito.data.local.Producto
import com.example.proyectito.data.local.Sucursal

/** Datos de ejemplo que se insertan la primera vez que se abre la app. */
object DatosIniciales {

    val productos = listOf(
        Producto(1, "Espresso", "Shot intenso de café de altura, con crema espesa y notas a chocolate amargo.", 35.0, "Café", "☕", 0xFFB9855A),
        Producto(2, "Americano", "Espresso doble alargado con agua caliente. Suave y limpio.", 40.0, "Café", "☕", 0xFFA67449),
        Producto(3, "Capuchino", "Espresso con leche vaporizada y una nube de espuma cremosa.", 55.0, "Café", "🥛", 0xFFD2A679),
        Producto(4, "Latte vainilla", "Leche texturizada, espresso y un toque de jarabe artesanal de vainilla.", 60.0, "Café", "🍦", 0xFFE0BC8E),
        Producto(5, "Moka", "Chocolate oscuro derretido, espresso y leche. El favorito de los golosos.", 65.0, "Café", "🍫", 0xFF8D5E45),
        Producto(6, "Cold brew", "Café macerado en frío por 18 horas. Refrescante y de poco amargor.", 58.0, "Frío", "🧊", 0xFF7FA3A0),
        Producto(7, "Frappé caramelo", "Café frappé con salsa de caramelo, crema batida y hielo triturado.", 72.0, "Frío", "🥤", 0xFFD9A15B),
        Producto(8, "Té chai helado", "Té negro especiado con canela y cardamomo, servido con hielo.", 55.0, "Frío", "🍵", 0xFF9DB58A),
        Producto(9, "Croissant", "Hojaldre de mantequilla horneado cada mañana. Crujiente por fuera.", 45.0, "Postres", "🥐", 0xFFE3B25F),
        Producto(10, "Pastel de zanahoria", "Rebanada húmeda con nueces y betún de queso crema.", 68.0, "Postres", "🍰", 0xFFE79A5B),
        Producto(11, "Galleta con chispas", "Galleta grande con chispas de chocolate semiamargo, suave al centro.", 30.0, "Postres", "🍪", 0xFFC98F5A),
        Producto(12, "Cheesecake de frutos rojos", "Base de galleta, queso cremoso y mermelada de frutos rojos.", 75.0, "Postres", "🧁", 0xFFD88A9A)
    )

    val sucursales = listOf(
        Sucursal(1, "Cafecito Roma", "Calle Orizaba 101, Roma Norte", "Lun - Dom · 7:00 a 22:00", 19.4194, -99.1627),
        Sucursal(2, "Cafecito Condesa", "Av. Ámsterdam 240, Condesa", "Lun - Dom · 7:30 a 22:30", 19.4112, -99.1737),
        Sucursal(3, "Cafecito Coyoacán", "Calle Allende 12, Centro de Coyoacán", "Lun - Dom · 8:00 a 21:00", 19.3500, -99.1620),
        Sucursal(4, "Cafecito Polanco", "Av. Presidente Masaryk 360, Polanco", "Lun - Sáb · 7:00 a 23:00", 19.4338, -99.1953)
    )
}
