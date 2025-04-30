package com.example.wherenow.data.usecases

import com.example.wherenow.repository.file.FileRepository

class DeleteFileUseCase internal constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(id: Int) = fileRepository.deleteFile(id)
}