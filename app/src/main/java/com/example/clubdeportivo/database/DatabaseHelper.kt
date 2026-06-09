package com.example.clubdeportivo.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubdeportivo.models.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ClubDeportivo.db"
        private const val DATABASE_VERSION = 4
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
        CREATE TABLE usuarios(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            username TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL,
            rol TEXT NOT NULL,
            email TEXT,
            telefono TEXT,
            sede TEXT
        )
    """)
        db.execSQL("""
        CREATE TABLE persona(
            idPersona INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            apellido TEXT NOT NULL,
            fechaNacimiento TEXT,
            direccion TEXT,
            dni TEXT UNIQUE NOT NULL,
            telefono TEXT,
            aptoFisico INTEGER DEFAULT 0
        )
    """)
        db.execSQL("""
        CREATE TABLE actividad(
            idActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            precio REAL NOT NULL
        )
    """)
        db.execSQL("""
        CREATE TABLE socio(
            carnetNumero INTEGER PRIMARY KEY AUTOINCREMENT,
            idPersona INTEGER NOT NULL,
            fechaInscripcion TEXT NOT NULL,
            activo INTEGER DEFAULT 1,

            FOREIGN KEY(idPersona)
            REFERENCES persona(idPersona)
        )
    """)
        db.execSQL("""
        CREATE TABLE nosocio(
            carnetTemporal INTEGER PRIMARY KEY AUTOINCREMENT,
            idPersona INTEGER NOT NULL,

            FOREIGN KEY(idPersona)
            REFERENCES persona(idPersona)
        )
    """)
        db.execSQL("""
        CREATE TABLE cuota(
            idCuota INTEGER PRIMARY KEY AUTOINCREMENT,
            carnetNumero INTEGER NOT NULL,
            fechaGeneracion TEXT,
            fechaVencimiento TEXT,
            importe REAL,
            estado INTEGER DEFAULT 0,

            FOREIGN KEY(carnetNumero)
            REFERENCES socio(carnetNumero)
        )
    """)
        db.execSQL("""
        CREATE TABLE pago(
            idPago INTEGER PRIMARY KEY AUTOINCREMENT,
            fechaPago TEXT,
            importe REAL,
            metodoPago TEXT,
            observaciones TEXT
        )
    """)
        db.execSQL("""
        CREATE TABLE pago_cuota(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            idPago INTEGER,
            idCuota INTEGER,

            FOREIGN KEY(idPago)
            REFERENCES pago(idPago),

            FOREIGN KEY(idCuota)
            REFERENCES cuota(idCuota)
        )
    """)
        db.execSQL("""
        CREATE TABLE pago_actividad(
            idPagoActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            carnetTemporal INTEGER,
            idActividad INTEGER,
            fecha TEXT,
            importe REAL,
            estado INTEGER DEFAULT 0,

            FOREIGN KEY(carnetTemporal)
            REFERENCES nosocio(carnetTemporal),

            FOREIGN KEY(idActividad)
            REFERENCES actividad(idActividad)
        )
    """)
        insertarDatosIniciales(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS pago_actividad")
        db.execSQL("DROP TABLE IF EXISTS pago_cuota")
        db.execSQL("DROP TABLE IF EXISTS pago")
        db.execSQL("DROP TABLE IF EXISTS cuota")
        db.execSQL("DROP TABLE IF EXISTS nosocio")
        db.execSQL("DROP TABLE IF EXISTS socio")
        db.execSQL("DROP TABLE IF EXISTS actividad")
        db.execSQL("DROP TABLE IF EXISTS persona")
        db.execSQL("DROP TABLE IF EXISTS usuarios")

        onCreate(db)
    }
    private fun insertarDatosIniciales(
        db: SQLiteDatabase
    ) {

        val admin = ContentValues().apply {

            put("nombre","Administrador")
            put("username","admin")
            put("password","admin123")
            put("rol","administrador-general")
            put("email","admin@club.com")
            put("telefono","0000")
            put("sede","Central")
        }

        db.insert("usuarios",null,admin)

        db.execSQL("""
        INSERT INTO actividad(nombre,precio)
        VALUES
        ('Todas',25000),
        ('Futbol',5000),
        ('Voley',4500),
        ('Basquet',5500),
        ('Natacion',7000),
        ('Padel',6000)
    """)

        // Socio de prueba con deuda
        val personaTest = ContentValues().apply {
            put("nombre", "Carlos")
            put("apellido", "Deudor")
            put("fechaNacimiento", "1990-01-01")
            put("direccion", "Calle Falsa 123")
            put("dni", "12345678")
            put("telefono", "1122334455")
            put("aptoFisico", 1)
        }
        val idPersonaTest = db.insert("persona", null, personaTest)

        val socioTest = ContentValues().apply {
            put("idPersona", idPersonaTest)
            put("fechaInscripcion", "2024-01-01")
            put("activo", 1)
        }
        val carnetTest = db.insert("socio", null, socioTest)

        val cuotaTest = ContentValues().apply {
            put("carnetNumero", carnetTest)
            put("fechaGeneracion", "2024-01-01")
            put("fechaVencimiento", "2024-02-01")
            put("importe", 15000.0)
            put("estado", 0) // PENDIENTE
        }
        db.insert("cuota", null, cuotaTest)
    }

    fun autenticar(
        username: String,
        password: String
    ): Usuario? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM usuarios
        WHERE username = ?
        AND password = ?
        """,
            arrayOf(username, password)
        )

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {

            usuario = Usuario(
                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                username = cursor.getString(
                    cursor.getColumnIndexOrThrow("username")
                ),
                password = cursor.getString(
                    cursor.getColumnIndexOrThrow("password")
                ),
                rol = cursor.getString(
                    cursor.getColumnIndexOrThrow("rol")
                ),
                email = cursor.getString(
                    cursor.getColumnIndexOrThrow("email")
                ),
                telefono = cursor.getString(
                    cursor.getColumnIndexOrThrow("telefono")
                ),
                sede = cursor.getString(
                    cursor.getColumnIndexOrThrow("sede")
                )
            )
        }

        cursor.close()

        return usuario
    }
    fun insertarUsuario(
        usuario: Usuario
    ): Long {

        val db = writableDatabase

        val values = ContentValues().apply {

            put("nombre", usuario.nombre)
            put("username", usuario.username)
            put("password", usuario.password)
            put("rol", usuario.rol)
            put("email", usuario.email)
            put("telefono", usuario.telefono)
            put("sede", usuario.sede)
        }

        return db.insert(
            "usuarios",
            null,
            values
        )
    }

    ///usuarios
    fun obtenerTodosLosUsuarios(): List<Usuario> {

        val lista = mutableListOf<Usuario>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM usuarios
        ORDER BY nombre
        """,
            null
        )

        while (cursor.moveToNext()) {

            lista.add(
                Usuario(
                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),
                    nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre")
                    ),
                    username = cursor.getString(
                        cursor.getColumnIndexOrThrow("username")
                    ),
                    password = cursor.getString(
                        cursor.getColumnIndexOrThrow("password")
                    ),
                    rol = cursor.getString(
                        cursor.getColumnIndexOrThrow("rol")
                    ),
                    email = cursor.getString(
                        cursor.getColumnIndexOrThrow("email")
                    ),
                    telefono = cursor.getString(
                        cursor.getColumnIndexOrThrow("telefono")
                    ),
                    sede = cursor.getString(
                        cursor.getColumnIndexOrThrow("sede")
                    )
                )
            )
        }

        cursor.close()

        return lista
    }

    fun actualizarUsuario(
        usuario: Usuario
    ): Int {

        val db = writableDatabase

        val values = ContentValues().apply {

            put("nombre", usuario.nombre)
            put("username", usuario.username)
            put("password", usuario.password)
            put("rol", usuario.rol)
            put("email", usuario.email)
            put("telefono", usuario.telefono)
            put("sede", usuario.sede)
        }

        return db.update(
            "usuarios",
            values,
            "id=?",
            arrayOf(usuario.id.toString())
        )
    }

    fun eliminarUsuario(
        idUsuario: Int
    ): Int {

        val db = writableDatabase

        return db.delete(
            "usuarios",
            "id=?",
            arrayOf(idUsuario.toString())
        )
    }

    ////actividades
    fun obtenerActividades(): List<Actividad> {

        val lista = mutableListOf<Actividad>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM actividad
        ORDER BY nombre
        """,
            null
        )

        while (cursor.moveToNext()) {

            lista.add(
                Actividad(
                    idActividad = cursor.getInt(
                        cursor.getColumnIndexOrThrow("idActividad")
                    ),
                    nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre")
                    ),
                    precio = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("precio")
                    )
                )
            )
        }

        cursor.close()

        return lista
    }

    fun obtenerActividadPorId(
        idActividad: Int
    ): Actividad? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM actividad
        WHERE idActividad = ?
        """,
            arrayOf(idActividad.toString())
        )

        var actividad: Actividad? = null

        if (cursor.moveToFirst()) {

            actividad = Actividad(
                idActividad = cursor.getInt(
                    cursor.getColumnIndexOrThrow("idActividad")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                precio = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("precio")
                )
            )
        }

        cursor.close()

        return actividad
    }

    //persona
    fun insertarPersona(
        persona: Persona
    ): Long {

        val db = writableDatabase

        val values = ContentValues().apply {

            put("nombre", persona.nombre)
            put("apellido", persona.apellido)
            put("fechaNacimiento", persona.fechaNacimiento)
            put("direccion", persona.direccion)
            put("dni", persona.dni)
            put("telefono", persona.telefono)
            put(
                "aptoFisico",
                if (persona.aptoFisico) 1 else 0
            )
        }

        return db.insert(
            "persona",
            null,
            values
        )
    }

    fun obtenerPersonaPorDni(
        dni: String
    ): Persona? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM persona
        WHERE dni = ?
        """,
            arrayOf(dni)
        )

        var persona: Persona? = null

        if (cursor.moveToFirst()) {

            persona = Persona(
                idPersona = cursor.getLong(
                    cursor.getColumnIndexOrThrow("idPersona")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                apellido = cursor.getString(
                    cursor.getColumnIndexOrThrow("apellido")
                ),
                fechaNacimiento = cursor.getString(
                    cursor.getColumnIndexOrThrow("fechaNacimiento")
                ),
                direccion = cursor.getString(
                    cursor.getColumnIndexOrThrow("direccion")
                ),
                dni = cursor.getString(
                    cursor.getColumnIndexOrThrow("dni")
                ),
                telefono = cursor.getString(
                    cursor.getColumnIndexOrThrow("telefono")
                ),
                aptoFisico =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("aptoFisico")
                    ) == 1
            )
        }

        cursor.close()

        return persona
    }

    //socio
    fun registrarSocio(
        persona: Persona,
        fechaInscripcion: String,
        fechaVencimiento: String,
        importeMensual: Double
    ): Long {
        val db = writableDatabase
        var carnetNumero: Long = -1

        db.beginTransaction()
        try {
            val idPersona = insertarPersonaConDb(db, persona)
            if (idPersona <= 0) return -1

            val values = ContentValues().apply {
                put("idPersona", idPersona)
                put("fechaInscripcion", fechaInscripcion)
                put("activo", 1)
            }

            carnetNumero = db.insert("socio", null, values)

            if (carnetNumero > 0) {
                generarCuotaConDb(
                    db,
                    carnetNumero,
                    fechaInscripcion,
                    fechaVencimiento,
                    importeMensual
                )
                db.setTransactionSuccessful()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            carnetNumero = -1
        } finally {
            db.endTransaction()
        }

        return carnetNumero
    }

    // Funciones auxiliares que aceptan DB para transacciones
    private fun insertarPersonaConDb(db: SQLiteDatabase, persona: Persona): Long {
        val values = ContentValues().apply {
            put("nombre", persona.nombre)
            put("apellido", persona.apellido)
            put("fechaNacimiento", persona.fechaNacimiento)
            put("direccion", persona.direccion)
            put("dni", persona.dni)
            put("telefono", persona.telefono)
            put("aptoFisico", if (persona.aptoFisico) 1 else 0)
        }
        return db.insert("persona", null, values)
    }

    private fun generarCuotaConDb(
        db: SQLiteDatabase,
        carnetNumero: Long,
        fechaGeneracion: String,
        fechaVencimiento: String,
        importe: Double
    ): Long {
        val values = ContentValues().apply {
            put("carnetNumero", carnetNumero)
            put("fechaGeneracion", fechaGeneracion)
            put("fechaVencimiento", fechaVencimiento)
            put("importe", importe)
            put("estado", 0)
        }
        return db.insert("cuota", null, values)
    }

    fun obtenerSocioPorCarnet(
        carnetNumero: Long
    ): Socio? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT
            s.carnetNumero,
            s.idPersona,
            s.fechaInscripcion,
            s.activo,

            p.nombre,
            p.apellido,
            p.dni,
            p.telefono,
            p.direccion,
            p.fechaNacimiento,
            p.aptoFisico

        FROM socio s

        INNER JOIN persona p
            ON p.idPersona = s.idPersona

        WHERE s.carnetNumero = ?
        """,
            arrayOf(carnetNumero.toString())
        )

        var socio: Socio? = null

        if (cursor.moveToFirst()) {

            socio = Socio(
                carnetNumero =
                    cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            "carnetNumero"
                        )
                    ),

                idPersona =
                    cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            "idPersona"
                        )
                    ),

                nombre =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "nombre"
                        )
                    ),

                apellido =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "apellido"
                        )
                    ),

                dni =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "dni"
                        )
                    ),

                telefono =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "telefono"
                        )
                    ),

                direccion =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "direccion"
                        )
                    ),

                fechaNacimiento =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "fechaNacimiento"
                        )
                    ),

                aptoFisico =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            "aptoFisico"
                        )
                    ) == 1,

                fechaInscripcion =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "fechaInscripcion"
                        )
                    ),

                activo =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            "activo"
                        )
                    ) == 1
            )
        }

        cursor.close()

        return socio
    }
    fun buscarSocios(
        filtro: String = ""
    ): List<Socio> {

        val lista = mutableListOf<Socio>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT

            s.carnetNumero,
            s.idPersona,
            s.fechaInscripcion,
            s.activo,

            p.nombre,
            p.apellido,
            p.dni,
            p.telefono,
            p.direccion,
            p.fechaNacimiento,
            p.aptoFisico

        FROM socio s

        INNER JOIN persona p
            ON p.idPersona = s.idPersona

        WHERE

            p.nombre LIKE ?
            OR p.apellido LIKE ?
            OR p.dni LIKE ?

        ORDER BY p.apellido,p.nombre
        """,
            arrayOf(
                "%$filtro%",
                "%$filtro%",
                "%$filtro%"
            )
        )

        while (cursor.moveToNext()) {

            val carnetNumero =
                cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                        "carnetNumero"
                    )
                )

            val cuota =
                obtenerCuotaActual(
                    carnetNumero
                )

            lista.add(

                Socio(

                    carnetNumero =
                        carnetNumero,

                    idPersona =
                        cursor.getLong(
                            cursor.getColumnIndexOrThrow(
                                "idPersona"
                            )
                        ),

                    nombre =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "nombre"
                            )
                        ),

                    apellido =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "apellido"
                            )
                        ),

                    dni =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "dni"
                            )
                        ),

                    telefono =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "telefono"
                            )
                        ),

                    direccion =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "direccion"
                            )
                        ),

                    fechaNacimiento =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "fechaNacimiento"
                            )
                        ),

                    aptoFisico =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "aptoFisico"
                            )
                        ) == 1,

                    fechaInscripcion =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                "fechaInscripcion"
                            )
                        ),

                    activo =
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                "activo"
                            )
                        ) == 1,

                    vencimiento =
                        cuota?.vencimiento ?: ""
                )
            )
        }

        cursor.close()

        return lista
    }
    fun obtenerSocios(): List<Socio> {
        return buscarSocios("")
    }
    fun obtenerSociosActivos(): List<Socio> {

        return buscarSocios()
            .filter { it.activo }
    }

    fun registrarNoSocio(
        persona: Persona,
        actividad: Actividad,
        fecha: String
    ): Long {
        val db = writableDatabase
        var carnetTemporal: Long = -1

        db.beginTransaction()
        try {
            val idPersona = insertarPersonaConDb(db, persona)
            if (idPersona <= 0) return -1

            val values = ContentValues().apply {
                put("idPersona", idPersona)
            }

            carnetTemporal = db.insert("nosocio", null, values)

            if (carnetTemporal > 0) {
                val idPago = generarPagoActividadConDb(
                    db,
                    carnetTemporal,
                    actividad.idActividad,
                    actividad.precio,
                    fecha
                )
                if (idPago > 0) {
                    db.setTransactionSuccessful()
                } else {
                    carnetTemporal = -1
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            carnetTemporal = -1
        } finally {
            db.endTransaction()
        }
        return carnetTemporal
    }

    private fun generarPagoActividadConDb(
        db: SQLiteDatabase,
        carnetTemporal: Long,
        idActividad: Int,
        importe: Double,
        fecha: String
    ): Long {
        val values = ContentValues().apply {
            put("carnetTemporal", carnetTemporal)
            put("idActividad", idActividad)
            put("fecha", fecha)
            put("importe", importe)
            put("estado", 0)
        }
        return db.insert("pago_actividad", null, values)
    }


    fun generarCuota(
        carnetNumero: Long,
        fechaGeneracion: String,
        fechaVencimiento: String,
        importe: Double
    ): Long {

        val db = writableDatabase

        val values = ContentValues().apply {

            put(
                "carnetNumero",
                carnetNumero
            )

            put(
                "fechaGeneracion",
                fechaGeneracion
            )

            put(
                "fechaVencimiento",
                fechaVencimiento
            )

            put(
                "importe",
                importe
            )

            put(
                "estado",
                0
            )
        }

        return db.insert(
            "cuota",
            null,
            values
        )
    }
    fun generarPagoActividad(
        carnetTemporal: Long,
        idActividad: Int,
        importe: Double,
        fecha: String
    ): Long {

        val db = writableDatabase

        val values = ContentValues().apply {

            put(
                "carnetTemporal",
                carnetTemporal
            )

            put(
                "idActividad",
                idActividad
            )

            put(
                "fecha",
                fecha
            )

            put(
                "importe",
                importe
            )

            // 0 = Pendiente
            // 1 = Pagado
            put(
                "estado",
                0
            )
        }

        return db.insert(
            "pago_actividad",
            null,
            values
        )
    }
    fun obtenerCuotaActual(
        carnetNumero: Long
    ): Cuota? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM cuota
        WHERE carnetNumero = ?
        ORDER BY idCuota DESC
        LIMIT 1
        """,
            arrayOf(carnetNumero.toString())
        )

        var cuota: Cuota? = null

        if (cursor.moveToFirst()) {

            cuota = Cuota(
                idCuota =
                    cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            "idCuota"
                        )
                    ),

                carnetNumero =
                    cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            "carnetNumero"
                        )
                    ),

                fechaGeneracion =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "fechaGeneracion"
                        )
                    ),

                vencimiento =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "fechaVencimiento"
                        )
                    ),

                importe =
                    cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                            "importe"
                        )
                    ),

                estado =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            "estado"
                        )
                    )
            )
        }

        cursor.close()

        return cuota
    }

    fun obtenerPagoActividad(
        carnetTemporal: Long
    ): Cursor {

        val db = readableDatabase

        return db.rawQuery(
            """
        SELECT
            pa.idPagoActividad,
            pa.fecha,
            pa.importe,
            pa.estado,
            a.nombre AS actividad

        FROM pago_actividad pa

        INNER JOIN actividad a
            ON a.idActividad = pa.idActividad

        WHERE pa.carnetTemporal = ?

        ORDER BY pa.idPagoActividad DESC
        LIMIT 1
        """,
            arrayOf(carnetTemporal.toString())
        )
    }

    fun marcarPagoActividadPagado(
        idPagoActividad: Long
    ): Int {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("estado", 1)
        }

        return db.update(
            "pago_actividad",
            values,
            "idPagoActividad=?",
            arrayOf(idPagoActividad.toString())
        )
    }
    fun registrarPagoCuota(
        idCuota: Long,
        importe: Double,
        metodoPago: String,
        observaciones: String = ""
    ): Long {

        val db = writableDatabase

        val valuesPago = ContentValues().apply {

            put(
                "fechaPago",
                System.currentTimeMillis().toString()
            )

            put(
                "importe",
                importe
            )

            put(
                "metodoPago",
                metodoPago
            )

            put(
                "observaciones",
                observaciones
            )
        }

        val idPago =
            db.insert(
                "pago",
                null,
                valuesPago
            )

        if (idPago > 0) {

            val relacion =
                ContentValues().apply {

                    put(
                        "idPago",
                        idPago
                    )

                    put(
                        "idCuota",
                        idCuota
                    )
                }

            db.insert(
                "pago_cuota",
                null,
                relacion
            )

            marcarCuotaPagada(
                idCuota
            )
        }

        return idPago
    }
    fun marcarCuotaPagada(
        idCuota: Long
    ): Int {

        val db = writableDatabase

        val values = ContentValues().apply {

            put(
                "estado",
                1
            )
        }

        return db.update(
            "cuota",
            values,
            "idCuota=?",
            arrayOf(
                idCuota.toString()
            )
        )
    }
    fun obtenerCuotaPendiente(
        carnetNumero: Long
    ): Cuota? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM cuota
        WHERE carnetNumero = ?
        AND estado = 0
        ORDER BY idCuota DESC
        LIMIT 1
        """,
            arrayOf(
                carnetNumero.toString()
            )
        )

        var cuota: Cuota? = null

        if (cursor.moveToFirst()) {

            cuota = Cuota(

                idCuota =
                    cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            "idCuota"
                        )
                    ),

                carnetNumero =
                    cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            "carnetNumero"
                        )
                    ),

                fechaGeneracion =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "fechaGeneracion"
                        )
                    ),

                vencimiento =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "fechaVencimiento"
                        )
                    ),

                importe =
                    cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                            "importe"
                        )
                    ),

                estado =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            "estado"
                        )
                    )
            )
        }

        cursor.close()

        return cuota
    }

    fun obtenerUsuarioPorUsername(
        username: String
    ): Usuario? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM usuarios
        WHERE username = ?
        LIMIT 1
        """,
            arrayOf(username)
        )

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {

            usuario = Usuario(
                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                username = cursor.getString(
                    cursor.getColumnIndexOrThrow("username")
                ),
                password = cursor.getString(
                    cursor.getColumnIndexOrThrow("password")
                ),
                rol = cursor.getString(
                    cursor.getColumnIndexOrThrow("rol")
                ),
                email = cursor.getString(
                    cursor.getColumnIndexOrThrow("email")
                ),
                telefono = cursor.getString(
                    cursor.getColumnIndexOrThrow("telefono")
                ),
                sede = cursor.getString(
                    cursor.getColumnIndexOrThrow("sede")
                )
            )
        }

        cursor.close()

        return usuario
    }


    fun buscarSociosDeudores(filtro: String = ""): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT 
            s.carnetNumero, s.idPersona, s.fechaInscripcion, s.activo,
            p.nombre, p.apellido, p.dni, p.telefono, p.direccion, p.fechaNacimiento, p.aptoFisico
        FROM socio s
        INNER JOIN persona p ON p.idPersona = s.idPersona
        INNER JOIN cuota c ON s.carnetNumero = c.carnetNumero
        WHERE c.estado = 0 
        AND (p.nombre LIKE ? OR p.apellido LIKE ? OR p.dni LIKE ?)
        ORDER BY p.apellido, p.nombre
        """,
            arrayOf("%$filtro%", "%$filtro%", "%$filtro%")
        )

        while (cursor.moveToNext()) {
            val carnetNumero = cursor.getLong(cursor.getColumnIndexOrThrow("carnetNumero"))
            val cuota = obtenerCuotaActual(carnetNumero)
            lista.add(Socio(
                carnetNumero = carnetNumero,
                idPersona = cursor.getLong(cursor.getColumnIndexOrThrow("idPersona")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento")),
                aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("aptoFisico")) == 1,
                fechaInscripcion = cursor.getString(cursor.getColumnIndexOrThrow("fechaInscripcion")),
                activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo")) == 1,
                vencimiento = cuota?.vencimiento ?: ""
            ))
        }
        cursor.close()
        return lista
    }

    fun buscarNoSocios(filtro: String = ""): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT 
                ns.carnetTemporal, ns.idPersona,
                p.nombre, p.apellido, p.dni, p.telefono, p.direccion, p.fechaNacimiento, p.aptoFisico
            FROM nosocio ns
            INNER JOIN persona p ON p.idPersona = ns.idPersona
            WHERE p.nombre LIKE ? OR p.apellido LIKE ? OR p.dni LIKE ?
            ORDER BY p.apellido, p.nombre
            """,
            arrayOf("%$filtro%", "%$filtro%", "%$filtro%")
        )

        while (cursor.moveToNext()) {
            lista.add(Socio(
                carnetNumero = cursor.getLong(cursor.getColumnIndexOrThrow("carnetTemporal")),
                idPersona = cursor.getLong(cursor.getColumnIndexOrThrow("idPersona")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento")),
                aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("aptoFisico")) == 1,
                fechaInscripcion = "",
                activo = true,
                vencimiento = "Pase Diario"
            ))
        }
        cursor.close()
        return lista
    }

    fun buscarNoSociosDeudores(filtro: String = ""): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT 
                ns.carnetTemporal, ns.idPersona,
                p.nombre, p.apellido, p.dni, p.telefono, p.direccion, p.fechaNacimiento, p.aptoFisico
            FROM nosocio ns
            INNER JOIN persona p ON p.idPersona = ns.idPersona
            INNER JOIN pago_actividad pa ON ns.carnetTemporal = pa.carnetTemporal
            WHERE pa.estado = 0
            AND (p.nombre LIKE ? OR p.apellido LIKE ? OR p.dni LIKE ?)
            ORDER BY p.apellido, p.nombre
            """,
            arrayOf("%$filtro%", "%$filtro%", "%$filtro%")
        )

        while (cursor.moveToNext()) {
            lista.add(Socio(
                carnetNumero = cursor.getLong(cursor.getColumnIndexOrThrow("carnetTemporal")),
                idPersona = cursor.getLong(cursor.getColumnIndexOrThrow("idPersona")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento")),
                aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("aptoFisico")) == 1,
                fechaInscripcion = "",
                activo = true,
                vencimiento = "Pase Diario"
            ))
        }
        cursor.close()
        return lista
    }

    fun obtenerPagoActividadPendiente(carnetTemporal: Long): Pair<Long, Double>? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT idPagoActividad, importe FROM pago_actividad WHERE carnetTemporal = ? AND estado = 0 LIMIT 1",
            arrayOf(carnetTemporal.toString())
        )
        var resultado: Pair<Long, Double>? = null
        if (cursor.moveToFirst()) {
            resultado = Pair(
                cursor.getLong(cursor.getColumnIndexOrThrow("idPagoActividad")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("importe"))
            )
        }
        cursor.close()
        return resultado
    }

    fun obtenerDeudores(): List<Map<String, Any>> {
        val lista = mutableListOf<Map<String, Any>>()
        val db = readableDatabase

        // Deudores Socios (Cuotas pendientes)
        val cursorSocios = db.rawQuery(
            """
            SELECT p.nombre, p.apellido, s.carnetNumero, c.fechaVencimiento, c.importe
            FROM socio s
            INNER JOIN persona p ON s.idPersona = p.idPersona
            INNER JOIN cuota c ON s.carnetNumero = c.carnetNumero
            WHERE c.estado = 0
            """, null
        )

        while (cursorSocios.moveToNext()) {
            lista.add(mapOf(
                "nombre" to "${cursorSocios.getString(0)} ${cursorSocios.getString(1)}",
                "detalles" to "N° S-${cursorSocios.getLong(2)} | Venc: ${cursorSocios.getString(3)}",
                "monto" to cursorSocios.getDouble(4)
            ))
        }
        cursorSocios.close()

        // Deudores No Socios (Pagos de actividad pendientes)
        val cursorNoSocios = db.rawQuery(
            """
            SELECT p.nombre, p.apellido, ns.carnetTemporal, pa.fecha, pa.importe
            FROM nosocio ns
            INNER JOIN persona p ON ns.idPersona = p.idPersona
            INNER JOIN pago_actividad pa ON ns.carnetTemporal = pa.carnetTemporal
            WHERE pa.estado = 0
            """, null
        )

        while (cursorNoSocios.moveToNext()) {
            lista.add(mapOf(
                "nombre" to "${cursorNoSocios.getString(0)} ${cursorNoSocios.getString(1)}",
                "detalles" to "N° NS-${cursorNoSocios.getLong(2)} | Fecha: ${cursorNoSocios.getString(3)}",
                "monto" to cursorNoSocios.getDouble(4)
            ))
        }
        cursorNoSocios.close()

        return lista
    }
}
