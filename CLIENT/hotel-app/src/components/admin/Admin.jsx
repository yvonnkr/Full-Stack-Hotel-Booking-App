import { Link } from "react-router-dom";

const Admin = () => {
  return (
    <section className="container mt-5">
      <h2>Welcome to Admin Panel</h2>
      <hr />
      <br />
      <Link
        to={"/existing-rooms"}
        style={{ textDecoration: "none" }}
        className={"hotel-color fw-bold fs-5"}
      >
        Manage Rooms
      </Link>{" "}
      <br />
      <Link
        to={"/existing-bookings"}
        style={{ textDecoration: "none" }}
        className={"hotel-color fw-bolder fs-5"}
      >
        Manage Bookings
      </Link>
    </section>
  );
};

export default Admin;
