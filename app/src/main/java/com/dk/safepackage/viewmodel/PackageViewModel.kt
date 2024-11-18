package com.dk.safepackage.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dk.safepackage.dao.PackageDao
import com.dk.safepackage.entities.PackageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PackageViewModel(private val packageDao: PackageDao):ViewModel(){
    val packages: Flow<List<PackageEntity>> = packageDao.getAllPackages()
    val packageCount: Flow<Int> = packageDao.getPackageCount()

    private val _selectedPackage = MutableStateFlow<PackageEntity?>(null)
    val selectedPackage = _selectedPackage.asStateFlow()

    private val _selectedPackages = MutableStateFlow<List<PackageEntity>>(emptyList())
    val selectedPackages = _selectedPackages.asStateFlow()
    fun insertPackage(packageEntity: PackageEntity){
        viewModelScope.launch {
            packageDao.insert(packageEntity)
        }
    }
    fun updatePackage(packageEntity: PackageEntity){
        viewModelScope.launch {
            packageDao.update(packageEntity)
        }
    }
    fun deletePackage(packageEntity: PackageEntity){
        viewModelScope.launch {
            packageDao.delete(packageEntity)
        }
    }
    fun getPackageById(id: Int): Flow<PackageEntity?> {
        return packageDao.getPackageById(id)
    }
    fun getPackagesFromDate(date: Long){
        viewModelScope.launch {
            packageDao.getPackagesFromDate(date).collect{
                packages -> _selectedPackages.value = packages
            }
        }
    }
    fun updatePackageStatus(id: Int, fechaHoraRetiro: Long, estado: Boolean){
        viewModelScope.launch {
            packageDao.updatePackageStatus(id, fechaHoraRetiro, estado)
        }
    }
}

class PackageViewModelFactory(private val packageDao: PackageDao): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(PackageViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return PackageViewModel(packageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}