create table if not exists item_categories (
    id serial primary key,
    name text,
	itemsId BIGINT,
	FOREIGN KEY (itemsId) REFERENCES item (Id) ON DELETE CASCADE,
	categoriesId BIGINT,
	FOREIGN KEY (categoriesId) REFERENCES categories (Id)
	);