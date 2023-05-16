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

  const div = document.getElementById('search-results');
  clearResultElements(div);

  if (results.length === 0) {
    div.appendChild(createNoResultsElement());
  } else {
    results.forEach(result => {
      div.appendChild(createResultElement(result));
    });
  }
}

function clearResultElements(div) {
  while (div.firstChild) {
    div.removeChild(div.firstChild);
  }
}

function createResultElement(result) {
  const p = document.createElement('p');

  const link = document.createElement('a');
  link.href = result.href;
  const title = document.createElement('p');
  title.innerText = result.title;
  title.className = 'mb-0';

  const type = document.createElement('small');
  type.innerText = result.type;

  link.appendChild(title);
  p.appendChild(link);
  p.appendChild(type);
  return p;
}

function createNoResultsElement() {
  const p = document.createElement('p');
  const small = document.createElement('small');
  small.className = 'is-italic';
  small.innerText = 'Your search yielded no results';

  p.appendChild(small);
  return p;
}
