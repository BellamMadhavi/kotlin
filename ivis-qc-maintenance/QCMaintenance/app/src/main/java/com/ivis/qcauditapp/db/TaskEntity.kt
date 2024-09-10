package com.ivis.qcauditapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks", primaryKeys = ["ticketId", "ticketTaskId"])
data class TaskEntity(
    val ticketId: Int,
    val ticketTaskId: Int,
    val taskstatus: String,
    val comments: String,
    val isSpareIndentUsed: String?,
    val fixedtype: String?,
    val fixedReason: String?,
    val openReason: String,
    val dependency: String?,
    val description: String )

@Entity(
    tableName = "spare_bag",
  /*  foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["ticketId"],
        childColumns = ["ticketId"],
        onDelete = ForeignKey.CASCADE
    )] */
)
data class SpareBagEntity(
    @PrimaryKey val id: Int,
    val ticketId: Int, // Foreign key to TaskEntity
    val taskId: Int,   // Task ID
    val reqType: String,
    val itemDescription: String,
    val qty: Int,
    val status: String,
    val serialNo: String,
    val docketNumber: String,
    val dcNo: String,
    val dispatchedDate: String,
    val receivedDate: String
)

@Entity(
    tableName = "ticket_images",
    /*foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["ticketId"],
        childColumns = ["ticketId"],
        onDelete = ForeignKey.CASCADE
    )]*/
    primaryKeys = ["ticketId", "taskId", "imageUri"]
)
data class TicketImageEntity(
    val ticketId: Int,
    val taskId: Int,
    val imageUri: String
)
@Entity(tableName = "raise_spare_indents",
    /*foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["ticketId"],
        childColumns = ["ticketId"],
        onDelete = ForeignKey.CASCADE
    )] */
)
data class RaiseIndentSpare(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketId: Int,
    val taskId: Int,
    val name: String,
    val qty: Int
)

