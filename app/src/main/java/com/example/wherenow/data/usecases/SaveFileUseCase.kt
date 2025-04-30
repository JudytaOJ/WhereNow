package com.example.wherenow.data.usecases

import com.example.wherenow.repository.file.FileRepository
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.repository.file.models.toFile

class SaveFileUseCase internal constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(file: FileData) = fileRepository.saveFile(file.toFile())
}