package dev.kavrin.spring_boot_crash_course.controller

import dev.kavrin.spring_boot_crash_course.database.model.Note
import dev.kavrin.spring_boot_crash_course.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

// GET: http://localhost:3000/notes?ownerId=123
// POST: http://localhost:3000/notes
// DELETE: http://localhost:3000/notes/123

@RestController
@RequestMapping("/api/notes")
class NoteController(
    private val noteRepository: NoteRepository,
) {

    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: String,
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: String,
        val createdAt: Instant,
    )

    @PostMapping
    fun save(
        @RequestBody body: NoteRequest
    ): NoteResponse {
        val note = noteRepository.save(
            body.toNote()
        )
        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) ownerId: String,
    ): List<NoteResponse> {
        return noteRepository.findByOwnerId(ObjectId(ownerId))
            .map { it.toResponse() }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
        @PathVariable id: String,
    ) {
        noteRepository.deleteById(ObjectId(id))
    }
}

private fun Note.toResponse(): NoteController.NoteResponse {
    return NoteController.NoteResponse(
        id = this.id.toHexString(),
        title = this.title,
        content = this.content,
        color = this.colorHex,
        createdAt = this.createdAt,
    )
}

private fun NoteController.NoteRequest.toNote(): Note {
    return Note(
        id = this.id?.let { ObjectId(it) } ?: ObjectId.get(),
        title = this.title,
        content = this.content,
        colorHex = this.color,
        createdAt = Instant.now(),
        ownerId = ObjectId(),
    )
}