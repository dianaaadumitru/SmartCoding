import React, { useEffect, useState } from 'react';
import './Lesson.css'
import { useNavigate } from 'react-router-dom';
import { AiFillSignal } from 'react-icons/ai';

const Lesson = ({courses}) => {  return (
      <div className="list-container-lesson">
          {courses.map((item) => (
              <div key={item.id} className="list-item-lesson">
                  <div className="list-item-header-lesson">Course</div>
                  <h3 className="list-name-lesson">{item.name}</h3>
                  <p className="list-description-lesson">{item.description}</p>
                  {/* <div className="list-item-footer"><AiFillSignal />{item.difficulty}</div> */}
              </div>
          ))}
      </div>
  );
}

export default Lesson;