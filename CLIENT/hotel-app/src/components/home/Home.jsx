import React from "react";
import MainHeader from "../layout/MainHeader.jsx";
import Parallax from "../common/Parallax.jsx";
import HotelService from "../common/HotelService.jsx";
import RoomCarousel from "../common/RoomCarousel.jsx";

const Home = () => {
  return (
    <section>
      <MainHeader />
      <div className="container">
        <RoomCarousel />
        <Parallax />
        <HotelService />
        <RoomCarousel />
        <Parallax />
        <RoomCarousel />
      </div>
    </section>
  );
};

export default Home;
