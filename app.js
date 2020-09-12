const vam_request = (url, method, name, password, vamsi, location = "") => {
  const r = new Request(`http://localhost:8089/${url}`, {
    method,
    body: { name, password },
  });
  fetch(r).then((res) => {
    vamsi(res, location);
  });
};
