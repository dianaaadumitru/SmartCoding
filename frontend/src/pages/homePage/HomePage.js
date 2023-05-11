import './HomePage.css'
// import backgroundImage from 'src/resources/background.jpg'
import backgroundImage from '../../resources/background2.jpg'
import { Link } from 'react-router-dom';
import HomePageNavBar from './homePageNavBar/HomePageNavBar';


function HomePage() {
    return (
        <body>
            <HomePageNavBar />
            <div className='content-block'>
                <div className='center'>
                    <h1 className='page-heading page-heading--small'>Smart coding</h1>
                    <p className='page-content page-content--small'>Solve problems, sharpen skills, succeed in programming.</p>
                    <button class="create-account-btn">Create Account</button>
                </div>
            </div>
        </body>
    )
}

export default HomePage;