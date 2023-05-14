import React from 'react';

import './HomePageNavBar.css'

function HomePageNavBar(props) {
    function handleListItemClick() {
        props.targetRef.current.scrollIntoView({ behavior: 'smooth' });
      }


    return (
        <div>
            <nav>
                <ul>
                    <li onClick={handleListItemClick}>Explore</li>
                    <li><a href="#">Developer</a></li>
                    <li><a href="/signin">Sign in</a></li>
                </ul>
            </nav>
        </div>
    )
}

export default HomePageNavBar;