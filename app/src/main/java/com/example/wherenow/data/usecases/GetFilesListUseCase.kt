package com.example.wherenow.data.usecases

import com.example.wherenow.repository.file.FileRepository
import com.example.wherenow.repository.file.models.FileData

class GetFilesListUseCase internal constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(): List<FileData> =
        fileRepository.getFilesList().map {
            FileData(
                name = it.name,
                id = it.id,
                url = it.url,
//                tripId = it.tripId
            )
        }
}