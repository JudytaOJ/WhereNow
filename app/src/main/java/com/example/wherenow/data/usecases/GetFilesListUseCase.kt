package com.example.wherenow.data.usecases

import com.example.wherenow.repository.file.FileRepository
import com.example.wherenow.repository.file.models.FileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFilesListUseCase internal constructor(
    private val fileRepository: FileRepository
) {
    operator fun invoke(): Flow<List<FileData>> =
        fileRepository.getFilesList().map {
            it.map { data ->
                FileData(
                    name = data.name,
                    id = data.id,
                    url = data.url,
                    tripId = data.tripId
                )
            }
        }
}