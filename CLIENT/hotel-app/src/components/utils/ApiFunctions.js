import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8080",
});

const token = localStorage.getItem("token");

export const getHeader = () => {
  return {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };
};

export const getHeaderFormData = () => {
  return {
    Authorization: `Bearer ${token}`,
    "Content-Type": "multipart/form-data",
  };
};

/* This function adds a new room to the database */
export async function addRoom(photo, roomType, roomPrice) {
  const formData = new FormData();
  formData.append("photo", photo);
  formData.append("roomType", roomType);
  formData.append("roomPrice", roomPrice);

  const response = await api.post("/rooms/add/new-room", formData, {
    headers: getHeaderFormData(),
  });

  return response.status === 201;
}

/* This function gets all room types from the database */
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
    const result = await api.delete(`/rooms/delete/room/${roomId}`, {
      headers: getHeader(),
    });
    return result.data;
  } catch (e) {
    throw new Error(`Error deleting room ${e.message}`);
  }
}

/* This function updates a room*/
export async function updateRoom(roomId, roomData) {
  const formData = new FormData();
  formData.append("roomType", roomData.roomType);
  formData.append("roomPrice", roomData.roomPrice);
  formData.append("photo", roomData.photo);

  return await api.put(`/rooms/update/${roomId}`, formData, {
    headers: getHeaderFormData(),
  });
}

/* This function gets a room by the id */
export async function getRoomById(roomId) {
  try {
    const result = await api.get(`/rooms/room/${roomId}`);
    return result.data;
  } catch (e) {
    throw new Error(`Error fetching room ${e.message}`);
  }
}

/* This function saves a new booking to the database */
export async function bookRoom(roomId, booking) {
  try {
    const response = await api.post(
      `/bookings/room/${roomId}/booking`,
      booking,
      { headers: getHeader() },
    );
    return response.data;
  } catch (error) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data.message);
    } else {
      throw new Error(`Error booking room : ${error.message}`);
    }
  }
}

/* This function gets all bookings from the database */
export async function getAllBookings() {
  try {
    const result = await api.get("/bookings/all-bookings", {
      headers: getHeader(),
    });
    return result.data;
  } catch (error) {
    throw new Error(`Error fetching bookings : ${error.message}`);
  }
}

/* This function gets booking by the confirmation code */
export async function getBookingByConfirmationCode(confirmationCode) {
  try {
    const result = await api.get(`/bookings/confirmation/${confirmationCode}`, {
      headers: getHeader(),
    });
    return result.data;
  } catch (error) {
    if (error.response.status === 401) {
      throw new Error(error.response.data.message);
    }

    if (error.response && error.response.data) {
      throw new Error(error.response.data);
    } else {
      console.log(`else: ${error.message}`);
      throw new Error(`Error finding booking : ${error}`);
    }
  }
}

/* This function cancels user's booking */
export async function cancelBooking(bookingId) {
  try {
    const result = await api.delete(`/bookings/booking/${bookingId}/delete`, {
      headers: getHeader(),
    });
    return result.data;
  } catch (error) {
    throw new Error(`Error cancelling booking :${error.message}`);
  }
}

/* This function gets all available rooms from the database with a given date and a room type */
export async function getAvailableRooms(checkInDate, checkOutDate, roomType) {
  return await api.get(
    `rooms/available-rooms?checkInDate=${checkInDate}
		&checkOutDate=${checkOutDate}&roomType=${roomType}`,
  );
}

/* This function register's a new user */
export async function registerUser(registrationData) {
  try {
    const response = await api.post("/auth/register-user", registrationData);
    return response.data;
  } catch (error) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data);
    } else {
      throw new Error(`User registration error: ${error.message}`);
    }
  }
}

/* This function login's a registered user */
export async function loginUser(loginData) {
  try {
    const response = await api.post("/auth/login", loginData);
    return response.status >= 200 && response.status < 300
      ? response.data
      : null;
  } catch (error) {
    console.log(error);
    return null;
  }
}

/* This is the function to get a single user */
export async function getUser(userId) {
  try {
    const response = await api.get(`/users/${userId}`, {
      headers: getHeader(),
    });
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

/* This function to deletes a user */
export async function deleteUser(userId) {
  try {
    const response = await api.delete(`/users/delete/${userId}`, {
      headers: getHeader(),
    });
    return response.data;
  } catch (error) {
    return error.message;
  }
}

/* This function gets a user bookings by the user id */
export async function getBookingsByUserId(userId) {
  try {
    const response = await api.get(`/bookings/user/${userId}/bookings`, {
      headers: getHeader(),
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching bookings:", error.message);
    throw new Error("Failed to fetch bookings");
  }
}
