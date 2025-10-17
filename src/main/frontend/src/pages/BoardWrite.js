import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "./BoardWrite.css";

function BoardWrite({ user }) {
  const [title, setTitle] = useState(""); //제목은 공백문자열
  const [content, setContent] = useState(""); //내용도 공백문자열
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault({ title, content }); //이벤트함수에서 무조건 넣어줌, 안넣으면 이벤트 실행시 페이지 새로고침됨
    //로그인한 유저만 글쓰기 허용
    if (!user) {
      //참이면 로그인하지 않은경우
      alert("로그인 후 사용가능합니다");
      return;
    }
    try {
      await api.post("/api/board", { title, content });
      alert("글작성 완료");
      navigate("/board");
    } catch (err) {
      if (err.response && err.response.status === 400) {
        //회원가입 400에러 발생
        setErrors(err.response.data); //에러 추출,errors에 저장
      } else {
        console.error(err);
        alert("글쓰기 실패");
      }
    }
  };

  return (
    <div className="write-container">
      <h2>글쓰기</h2>
      <form onSubmit={handleSubmit} className="write-form">
        <input
          type="text"
          placeholder="제목"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        {errors.title && <p style={{ color: "red" }}>{errors.title}</p>}
        <textarea
          placeholder="내용"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        {errors.content && <p style={{ color: "red" }}>{errors.content}</p>}
        <div className="button-group">
          <button type="submit">등록</button>
          <button type="button" onClick={() => navigate("/board")}>
            취소
          </button>{" "}
          {/*취소버튼()에 navi넣어라, 눌리면 navigate호출되는데 /board로 */}
        </div>
      </form>
    </div>
  );
}

export default BoardWrite;
