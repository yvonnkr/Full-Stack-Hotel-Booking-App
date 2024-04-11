import MainHeader from "../layout/MainHeader.jsx";
import Parallax from "../common/Parallax.jsx";
import HotelService from "../common/HotelService.jsx";
import RoomCarousel from "../common/RoomCarousel.jsx";
import RoomSearch from "../common/RoomSearch.jsx";

const Home = () => {
  return (
    <section>
      <MainHeader />
      <div className="container">
        <RoomSearch />
        <RoomCarousel />
        <Parallax />
        <HotelService />
        <RoomCarousel />
        <Parallax />
      </div>
    </section>
  );
};

export default Home;
