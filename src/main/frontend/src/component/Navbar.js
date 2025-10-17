import {Link} from "react-router-dom";
import "./Navbar.css";
function Navbar ({onLogout, user}) {
    return (
        <nav className="navbar">
            <div className="logo">LSM 홈페이지</div>
            <div className="menu">
                <Link to="/">Home</Link>
                <Link to="/board">게시판</Link>
                {!user && <Link to="/login">로그인</Link>}
                {!user && <Link to="/signup">회원가입</Link>} 
                {user && <span className="username">환영합니다, {user}님!</span>}
                {user && <button onClick={onLogout} className="logout-btn">로그아웃</button>}
            </div>
        </nav>
    );
}

export default Navbar;