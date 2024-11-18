package com.dk.safepackage.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "packages")
data class PackageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destinatario: String,
    val departamento: String,
    val tipoPaquete: String,
    val fechaHoraIngreso: Long,
    val fechaHoraRetiro: Long?, //Fecha y hora de retiro son opcionales, se actualizarán después
    val empresaTransporte: String,
    val estado: Boolean, //True=Entregado, False=Sin entregar
    val comentario: String? = null //Campo opcional para comentarios
)
