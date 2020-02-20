import { POST, JSON_CONTENT_TYPE } from "./util.js";

const BASE_URL = `${window.location.href}game`;

const newGame = async () => {
  const playerOneName = prompt("Enter name for Player One:", "Player Name");
  if (!playerOneName) {
    return;
  }
  const playerTwoName = prompt("Enter name for Player Two:", "Player Name");
  if (!playerTwoName) {
    return;
  }
  if (playerOneName == playerTwoName) {
    alert('Player names should be different!');
      return;
  }

  const url = `${BASE_URL}/start/${playerOneName}/${playerTwoName}`;
  const response = await fetch(url, {
    headers: {
      "Content-Type": JSON_CONTENT_TYPE
    },
    method: POST
  });
  const data = await response.json();

  if (response.ok) {
    const newGameId = data.gameId;
    window.location.href = `${BASE_URL}/play/${newGameId}`;
  } else {
    alert(data.message);
  }
};

document.getElementById("new-game-button").addEventListener("click", newGame);
