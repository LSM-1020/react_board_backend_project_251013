import { Route, Routes } from 'react-router-dom';
import './App.css';
import Navbar from './component/Navbar';
import Home from "./pages/Home";
import Board from "./pages/Board";
import Login from "./pages/Login";
import BoardDetail from "./pages/BoardDetail_bak";
import BoardWrite from "./pages/BoardWrite";
import Signup from "./pages/Signup";
import { useEffect, useState } from 'react';
import api from './api/axiosConfig';

function App() {
  const [user,setUser] = useState(null); //현재 로그인한 유저의 이름

  const checkUser = async ()=> {
    try {
      const res = await api.get("api/auth/me");
      setUser(res.data.username);
    } catch {
      setUser(null);
  }
  }
  useEffect(()=>{
    checkUser();
  },[]);

  const handleLogout = async ()=>{
    await api.post("/api/auth/logout"); //백엔드 시큐리티에서 이 요청이 들어가면 로그아웃
    setUser(null);
  }
 
  return (
    <div className="App">
    
     <Navbar user={user} onLogout={handleLogout} />
     <Routes>
      <Route path='/' element={<Home />} /> 
      {/* /*app에서 선언한것들을 타 페이지에서 쓰기위해 props를 넣어줌 ->board는 user가 필요하기에 user={user}로 써주면, board페이지에서 board 쓸수있음*/}
      <Route path='/board' element={<Board user={user}/>} /> 
      <Route path='/login' element={<Login onLogin={setUser}/>} />
      <Route path='/signup' element={<Signup />} />
      <Route path='/board/:id' element={<BoardDetail user={user} />} />
      <Route path='/board/Write' element={<BoardWrite user={user} />} />
     </Routes>
    
    </div>
  );
}

export default App;
