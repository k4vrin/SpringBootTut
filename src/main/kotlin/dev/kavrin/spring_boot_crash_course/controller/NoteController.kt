package dev.kavrin.spring_boot_crash_course.controller

import dev.kavrin.spring_boot_crash_course.database.model.Note
import dev.kavrin.spring_boot_crash_course.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
import java.time.Instant
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.core.context.SecurityContextHolder

// GET: http://localhost:3000/notes?ownerId=123
// POST: http://localhost:3000/notes
// DELETE: http://localhost:3000/notes/123

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes", description = "CRUD operations for notes")
class NoteController(
    private val noteRepository: NoteRepository,
) {

    data class NoteRequest(
        val id: String?,
        @field:NotBlank(
            message = "Title can't be blank."
        )
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
    @Operation(
        summary = "Create or update a note",
        description = "Creates a new note if id is null, otherwise updates the existing note.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "Note saved", content = [
                    Content(schema = Schema(implementation = NoteResponse::class))
                ]
            )
        ]
    )
    fun save(
        @Valid @RequestBody body: NoteRequest,
    ): NoteResponse {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val note = noteRepository.save(
            body.toNote(ownerId)
        )
        return note.toResponse()
    }

    @GetMapping
    @Operation(
        summary = "List notes by owner",
        description = "Returns all notes belonging to the given owner id"
    )
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return noteRepository.findByOwnerId(ObjectId(ownerId))
            .map { it.toResponse() }
    }

    @DeleteMapping(path = ["/{id}"])
    @Operation(
        summary = "Delete note by id",
        description = "Deletes the note with the provided id if it exists"
    )
    fun deleteById(
        @PathVariable id: String,
    ) {
        val note = noteRepository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("Note not found")
        }
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        if (note.ownerId.toHexString() == ownerId) {
            noteRepository.deleteById(ObjectId(id))
        }
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

private fun NoteController.NoteRequest.toNote(ownerId: String): Note {
    return Note(
        id = this.id?.let { ObjectId(it) } ?: ObjectId.get(),
        title = this.title,
        content = this.content,
        colorHex = this.color,
        createdAt = Instant.now(),
        ownerId = ObjectId(ownerId),
    )
}