function redirect(event, value, href) {
  if (event.key === 'Enter' && value.length >= 1) window.location.href = href + '?q=' + value;
}
