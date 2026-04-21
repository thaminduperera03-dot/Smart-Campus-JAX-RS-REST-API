package com.smartcampus.resource;

import com.smartcampus.DataStore;
import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.exception.RoomNotFoundException;
import com.smartcampus.model.Room;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
public class RoomResource {

    /*
     GET /api/v1/rooms - Getting all the rooms that are available
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return new ArrayList<>(DataStore.rooms.values());
    }

    /*
     POST /api/v1/rooms - Creating a new room
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room ID is required\"}")
                    .build();
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Room with this ID already exists\"}")
                    .build();
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    /*
     * GET /api/v1/rooms/{roomId} - Get a specific room by ID
     */
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room with ID '" + roomId + "' was not found.");
        }
        return room;
    }

    /*
      DELETE /api/v1/rooms/{roomId} - Delete a room (only if no sensors assigned)
     */
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room with ID '" + roomId + "' was not found.");
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Cannot delete room '" + roomId + "'. It still has "
                    + room.getSensorIds().size() + " active sensor(s) assigned to it. "
                    + "Please reassign or remove all sensors before deleting this room."
            );
        }
        DataStore.rooms.remove(roomId);
        return Response.ok("{\"message\":\"Room '" + roomId + "' successfully deleted.\"}").build();
    }

    @GET
    @Path("/trigger-error")
    @Produces(MediaType.APPLICATION_JSON)
    public Response triggerError() {
        throw new RuntimeException("Simulated unexpected server error for demo");
    }
}
