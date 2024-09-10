package com.ivis.qcauditapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(ticket: TaskEntity)

    @Query("SELECT * FROM tasks WHERE ticketId = :ticketId and ticketTaskId = :taskId")
    suspend fun getTaskByTicketIdTaskId(ticketId: Int, taskId:Int): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE ticketId = :ticketId ")
    suspend fun getTaskById(ticketId: Int): List<TaskEntity>

    @Update
    suspend fun updateTask(ticket: TaskEntity)

    @Query("DELETE FROM tasks WHERE ticketId = :ticketId ")
    suspend fun deleteTask(ticketId: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpareBags(spareBags: List<SpareBagEntity>)

    @Query("SELECT * FROM spare_bag WHERE ticketId = :ticketId AND taskId = :taskId")
    suspend fun getSpareBagsByTicketIdTaskId(ticketId: Int, taskId: Int): List<SpareBagEntity>

    @Query("SELECT * FROM spare_bag WHERE ticketId = :ticketId")
    suspend fun getSpareBagsByTicketId(ticketId: Int): List<SpareBagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(ticketImage: TicketImageEntity)
    @Update
    suspend fun updateImage(ticketImage: TicketImageEntity)

    @Query("SELECT * FROM ticket_images WHERE ticketId = :ticketId AND taskId = :taskId")
    suspend fun getImagesForTicket(ticketId: Int, taskId: Int): List<TicketImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRaiseIndentSpare(raiseIndentSpare: List<RaiseIndentSpare>)

    @Query("SELECT * FROM raise_spare_indents WHERE ticketId = :ticketId AND taskId = :taskId")
    suspend fun getrequestedIndentandSpares(ticketId: Int, taskId: Int): List<RaiseIndentSpare>

    @Query("SELECT * FROM raise_spare_indents WHERE id = :id AND ticketId = :ticketId AND taskId = :taskId LIMIT 1")
    suspend fun getRaiseIndentSpareById(id: Int, ticketId: Int, taskId: Int): RaiseIndentSpare?

    @Query("UPDATE raise_spare_indents SET qty = :newQuantity WHERE id = :id AND ticketId = :ticketId AND taskId = :taskId")
    fun updateQuantity(id: Int, ticketId: Int, taskId: Int, newQuantity: Int)

    @Query("DELETE FROM raise_spare_indents WHERE id = :id AND ticketId = :ticketId AND taskId = :taskId")
    suspend fun deleteRaiseIndentSpare(id: Int, ticketId: Int, taskId: Int)

    @Query("DELETE FROM ticket_images WHERE ticketId = :ticketId ")
    suspend fun deleteImagesByTicketId(ticketId: Int)

    // New method to delete a specific spare by ticketId, taskId, and spareId
    @Query("DELETE FROM spare_bag WHERE ticketId = :ticketId")
    suspend fun deleteSpareByTicketId(ticketId: Int)


    @Query("DELETE FROM raise_spare_indents WHERE ticketId = :ticketId")
    suspend fun deleteRaiseIndentSpareByTicketId(ticketId: Int)

    // New method to delete a specific spare by ticketId, taskId, and spareId
    @Query("DELETE FROM spare_bag WHERE ticketId = :ticketId AND taskId = :taskId AND id = :spareId")
    suspend fun deleteSpare(ticketId: Int, taskId: Int, spareId: Int)

    @Query("SELECT * FROM raise_spare_indents WHERE ticketId = :ticketId")
    suspend fun getRaiseIndentSparesByTicketId(ticketId: Int): List<RaiseIndentSpare>

    @Query("DELETE FROM tasks")
    suspend fun clearallTasks()

}