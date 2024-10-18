document.addEventListener('DOMContentLoaded', function () {
    loadBooks();
});

function loadBooks() {
    fetch('api/book')
        .then(response => response.json())
        .then(books => {
            const booksTableBody = document.getElementById('booksTableBody');
            booksTableBody.innerHTML = '';  // Очищаем таблицу перед добавлением

            books.forEach((book, index) => {
                let genres = book.genres.map(genre => genre.name).join(', ');
                booksTableBody.innerHTML += `
                    <tr>
                        <td>${book.title}</td>
                        <td>${index + 1}</td>
                        <td>${book.author.fullName}</td>
                        <td>${genres}</td>
                        <td>
                            <a href="/comment/${book.id}" class="btn btn-info btn-sm me-1">Comments</a>
                            <a href="/edit/${book.id}" class="btn btn-warning btn-sm me-1">Edit</a>
                            <form action="/delete/${book.id}" method="post" style="display:inline;">
                                <button type="submit" class="btn btn-danger btn-sm"
                                    onclick="return confirm('Are you sure you want to delete this book?');">
                                    Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(error => console.error('Error loading books:', error));
}

function loadBookInfo(bookId) {
    fetch(`/api/book/${bookId}`)
        .then(response => response.json())
        .then(book => {
            document.getElementById('title').value = book.title;
            document.getElementById('authorId').value = book.authorId;
            const selectedGenres = new Set(book.genreIds);
            document.querySelectorAll('#genresList input[type="checkbox"]').forEach(checkbox => {
                if (selectedGenres.has(parseInt(checkbox.value))) {
                    checkbox.checked = true;
                }
            });
        })
        .catch(error => console.error('Error loading book info:', error));
}

function loadAuthors() {
    fetch('api/authors')
        .then(response => response.json())
        .then(authors => {
            const tbody = document.querySelector('tbody'); // Находим tbody таблицы
            tbody.innerHTML = '';
            authors.forEach((author, index) => {
                tbody.innerHTML += `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${author.fullName}</td>
                    </tr>
                `;
            });
        })
        .catch(error => console.error('Error loading authors:', error));
}

function loadGenres() {
    fetch('api/genres')
        .then(response => response.json())
        .then(genres => {
            const tbody = document.querySelector('tbody'); // Находим tbody таблицы
            tbody.innerHTML = '';
            genres.forEach((genre, index) => {
                tbody.innerHTML += `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${genre.name}</td>
                    </tr>
                `;
            });
        })
        .catch(error => console.error('Error loading genres:', error));
}

function loadComments(bookId) {
    fetch(`/api/comments/${bookId}`) // Загружаем комментарии
        .then(response => response.json())
        .then(comments => {
            const commentsList = document.getElementById('commentsList');
            commentsList.innerHTML = ''; // Очищаем предыдущее содержимое
            comments.forEach((comment, index) => {
                commentsList.innerHTML += `
                    <div class="list-group-item">
                        <strong>${index + 1}</strong>.
                        <p>${comment.text}</p>
                    </div>
                `;
            });
        })
        .catch(error => console.error('Error loading comments:', error));
}



