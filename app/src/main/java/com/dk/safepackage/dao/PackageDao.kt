package com.dk.safepackage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dk.safepackage.entities.PackageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(packageEntity: PackageEntity)

    @Update
    suspend fun update(packageEntity: PackageEntity)

    @Delete
    suspend fun delete(packageEntity: PackageEntity)

    @Query("Select * From packages")
    fun getAllPackages(): Flow<List<PackageEntity>>

    @Query("Select * From packages Where id=:id")
    fun getPackageById(id: Int): Flow<PackageEntity?>

    @Query("Update packages Set fechaHoraRetiro=:fechaHoraRetiro, estado=:estado Where id=:id")
    suspend fun updatePackageStatus(id: Int, fechaHoraRetiro: Long, estado:Boolean)

    @Query("Select Count(*) from packages where estado=0")
    fun getPackageCount(): Flow<Int>

    @Query("""
    SELECT * FROM packages 
    WHERE DATE(fechaHoraIngreso / 1000, 'unixepoch','localtime') = DATE(:fechaHoraIngreso / 1000, 'unixepoch', 'localtime')
""")
    fun getPackagesFromDate(fechaHoraIngreso: Long): Flow<List<PackageEntity>>

    // Insertar Query para seleccionar paquetes desde la fecha indicada
}