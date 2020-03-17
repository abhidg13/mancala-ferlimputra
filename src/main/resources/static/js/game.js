import { POST, JSON_CONTENT_TYPE } from "./util.js";

const BASE_URL = `${window.location.origin}`;

const registerEventListener = () => {
    const pits = Array.from(document.getElementsByClassName("pit"));
    pits.forEach(pit => pit.addEventListener("click", pick));
};

const getPlayerNameByNumber = playerTurn => {
    const playerOneElement = document.getElementById("player1-name");
    const playerTwoElement = document.getElementById("player2-name");
    return playerTurn === 0
        ? playerOneElement.innerText
        : playerTwoElement.innerText;
};

const getPlayerNumberByName = playerName => {
    const playerOneElement = document.getElementById("player1-name");
    return playerName === playerOneElement.innerText ? 0 : 1;
};

const populateBoard = board => {
    board.forEach((stones, i) => {
        const pitElement = document.getElementById(`pit${i}`);
        pitElement.innerText = stones;
    });
};

const activateTurnDisplayIndicator = playerTurn => {
    //Set the color for the player row as per the turn
    const playerRow = document.getElementById(`player${playerTurn + 1}-row`);
    const opponentRow = document.getElementById(
        `player${playerTurn === 0 ? 2 : 1}-row`
    );

    playerRow.style.color = "rgb(1, 7, 74)";
    playerRow.style.background = "rgb(173, 180, 255)";

    opponentRow.style.color = "rgb(173, 180, 255)";
    opponentRow.style.background = "rgb(1, 7, 74)";

    //Make the player name blink as per the turn
    const playerNameElement = document.getElementById(`player${playerTurn + 1}-name`);
    const opponentNameElement = document.getElementById(
        `player${playerTurn === 0 ? 2 : 1}-name`
    );
    playerNameElement.classList.add("blink");
    opponentNameElement.classList.remove("blink");
};

const populateGame = data => {
    const gameStatusElement = document.getElementById("game-status");
    const totalTurnElement = document.getElementById("total-turn");
    const playerOneElement = document.getElementById("player1-name");
    const playerTwoElement = document.getElementById("player2-name");
    const playerOneLargePit = document.getElementById("large-pit0");
    const playerTwoLargePit = document.getElementById("large-pit1");
    const currentTurnElement = document.getElementById("player-turn");

    gameStatusElement.innerText = data.status;
    totalTurnElement.innerText = data.totalTurn;

    playerOneElement.innerText = data.playerOne.name;
    playerTwoElement.innerText = data.playerTwo.name;
    playerOneLargePit.innerText = data.playerOne.score;
    playerTwoLargePit.innerText = data.playerTwo.score;
    currentTurnElement.innerText = getPlayerNameByNumber(data.playerTurn);

    activateTurnDisplayIndicator(data.playerTurn);
    populateBoard(data.board);
};

const onLoad = async () => {
    registerEventListener();

    const url = `${BASE_URL}/game/${gameId}`;
    const response = await fetch(url);

    if (response.ok) {
        const data = await response.json();
        populateGame(data);
    }
};

const pick = async e => {
    const pitIndex = e.target.id.replace(/^\D+/g, "");
    const pickUrl = `${BASE_URL}/game/pick`;
    const currentPlayerName = document.getElementById("player-turn").innerText;

    const requestBody = {
        id: gameId,
        playerTurn: getPlayerNumberByName(currentPlayerName),
        index: pitIndex
    };

    const response = await fetch(pickUrl, {
        headers: {
            "Content-Type": JSON_CONTENT_TYPE
        },
        method: POST,
        body: JSON.stringify(requestBody)
    });
    if (response.ok) {
        const data = await response.json();
        populateGame(data);
    } else {
        response.text().then(function(object) {
            alert(object);
        })
    }
};

window.onload = onLoad;

document.getElementById("end-game-button").addEventListener("click",
    function(){
        window.location.href = `${BASE_URL}`;
    });
