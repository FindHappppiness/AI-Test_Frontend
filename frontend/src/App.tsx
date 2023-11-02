import { BrowserRouter, Route, Routes } from 'react-router-dom';
import GlobalStyle from '@styles/globalStyle';
import styled from 'styled-components';
import '@styles/font/font.css';
import Home from 'pages';
import Result from 'pages/result';
import Thanks from 'pages/thanks';
import ReactGA from "react-ga";

const gaTrackingId = import.meta.env.VITE_GOOGLE_ANALYTICS_ID;
ReactGA.initialize(gaTrackingId);

const App = () => {
	return (
		<AppContainer>
			<GlobalStyle />
			<BrowserRouter>
				<Routes>
					<Route path="/" element={<Home />} />
					<Route path="/result" element={<Result />} />
					<Route path="/thanks" element={<Thanks />} />
				</Routes>
			</BrowserRouter>
		</AppContainer>
	);
};

export default App;

const AppContainer = styled.div`
	background-color: rgba(255, 113, 116, 0.05);
	padding: 0;
	display: flex;
	width: 100vw;
	min-height: 100vh;

	-ms-overflow-style: none;
	scrollbar-width: none;
	&::-webkit-scrollbar {
		display: none;
		width: 0 !important;
	}
`;
