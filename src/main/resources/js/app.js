function openModal(id) {
  document.getElementById(id).classList.add('active');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('active')
}
function switchModal(element, nextModalID) {
  const currentID = extractID(element);
  if (currentID) closeModal(element);
  openModal(nextModalID);
}