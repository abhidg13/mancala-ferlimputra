import { POST, JSON_CONTENT_TYPE } from "./util.js";

const onLoad = () => {};

const pick = async e => {
  const pitIndex = e.id.replace(/^\D+/g, "");
  const pickUrl = `${BASE_URL}/pick`;
  const requestBody = {
    index: pitIndex
  };

  let response = await fetch(pickUrl, {
    headers: {
      "Content-Type": JSON_CONTENT_TYPE
    },
    method: POST,
    body: requestBody
  });

  if (response.ok) {
  }
};

window.onload = onLoad;
