window.onpageshow = function() {
  const el = document.getElementById('search');
  el.focus();

  const params = new URLSearchParams(window.location.search);
  const terms = params.get('q');

  if (terms) {
    el.value = terms;
    search(terms);
  }
};

function search(terms) {
  const results = idx.search(terms).map(item => {
    const document = documents.find(post => item.ref === post.href);
    return { score: item.score, href: document.href, type: document.type, title: document.title };
  });

  const ul = document.getElementById('search-results');
  results.forEach(result => {
    ul.appendChild(createLi(result));
  });
}

function createLi(result) {
  const li = document.createElement('li');
  const a1 = document.createElement('a');
  a1.href = result.href;
  a1.innerText = result.href;

  const a2 = document.createElement('a');
  a2.href = result.href;
  const h = document.createElement('h3');
  h.innerText = result.title;
  h.className = 'mb-0';

  a2.appendChild(h);
  li.appendChild(a1);
  li.appendChild(a2);
  li.append(result.type);
  return li;
}
