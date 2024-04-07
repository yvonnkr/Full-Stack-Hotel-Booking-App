import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8080",
});

/* This function adds a new room room to the database */
export async function addRoom(photo, roomType, roomPrice) {
  const formData = new FormData();
  formData.append("photo", photo);
  formData.append("roomType", roomType);
  formData.append("roomPrice", roomPrice);

  const response = await api.post("/rooms/add/new-room", formData);

  return response.status === 201;
}

/* This function gets all room types from thee database */
export async function getRoomTypes() {
  try {
    const response = await api.get("/rooms/room/types");
    return response.data;
  } catch (error) {
    throw new Error("Error fetching room types");
  }
}

/* This function gets all rooms from the database */
export async function getAllRooms() {
  try {
    const result = await api.get("/rooms/all-rooms");
    return result.data;
  } catch (error) {
    throw new Error("Error fetching rooms");
  }
}

/* This function deletes a room  by id from the database */
export async function deleteRoom(roomId) {
  try {
    const result = await api.delete(`/rooms/delete/room/${roomId}`);
    return result.data;
  } catch (e) {
    throw new Error(`Error deleting room ${e.message}`);
  }
}
