# 📘 HLD Fundamentals — Database Indexes (Deep Dive)

---

# 1. What is an Index

An index is a data structure used to improve the speed of data retrieval operations in a database.

It works similarly to an index in a book:

* Instead of scanning every page,
* We directly jump to the required location.

---

# 2. Why Indexes are Needed

Without indexes:

```text
SELECT * FROM users WHERE email='a@test.com'
```

Database may perform:
👉 Full Table Scan

Meaning:

* Check every row one by one

This becomes slow for large datasets.

---

# 3. What Indexes Improve

Indexes improve:

* SELECT queries
* WHERE filtering
* Sorting (ORDER BY)
* JOIN operations
* Range queries

---

# 4. Trade-offs of Indexes

Indexes are not free.

| Benefit       | Cost                       |
| ------------- | -------------------------- |
| Faster reads  | Slower writes              |
| Faster search | Extra storage              |
| Faster joins  | Index maintenance overhead |

---

# 5. How Indexes Work Internally

Most databases use:
👉 B-Tree or B+ Tree structures

Why?

* Balanced tree
* Efficient disk access
* Fast search/insert/delete

Complexity:

```text
Search: O(log n)
```

instead of:

```text
Full scan: O(n)
```

---

# 6. Primary Index

## Definition

Automatically created on primary key.

Example:

```sql
CREATE TABLE users (
  user_id INT PRIMARY KEY,
  name VARCHAR(100)
)
```

Database creates index on:

```text
user_id
```

---

## Characteristics

* Unique
* Not null
* One per table

---

# 7. Secondary Index

## Definition

Index created on non-primary columns.

Example:

```sql
CREATE INDEX idx_email ON users(email)
```

---

## Use Cases

* Searching by email
* Filtering queries

---

# 8. Clustered Index

## Definition

Physical data stored in same order as index.

Meaning:

* Table rows organized according to indexed column.

---

## Characteristics

* Only one clustered index possible
* Fast range queries

---

## Example

Rows stored ordered by:

```text
user_id
```

---

# 9. Non-Clustered Index

## Definition

Separate structure storing:

* Indexed column
* Pointer/reference to actual row

---

## Characteristics

* Multiple allowed
* Extra lookup sometimes needed

---

# 10. Composite Index

## Definition

Index on multiple columns.

Example:

```sql
CREATE INDEX idx_name_age ON users(name, age)
```

---

## Important Rule — Leftmost Prefix

Efficient for:

```text
(name)
(name, age)
```

But not efficient for:

```text
(age only)
```

---

# 11. Unique Index

## Definition

Prevents duplicate values.

Example:

```sql
CREATE UNIQUE INDEX idx_email ON users(email)
```

Ensures:

* No duplicate emails

---

# 12. Full-text Index

## Definition

Optimized for text search.

---

## Example Queries

```text
Search articles containing 'distributed systems'
```

---

## Use Cases

* Search engines
* Blog/article search

---

# 13. Hash Index

## Definition

Uses hash table internally.

---

## Characteristics

* Very fast equality lookups
* Poor for range queries

---

## Good For

```text
WHERE id = 10
```

---

## Bad For

```text
WHERE id > 10
```

---

# 14. Bitmap Index

## Definition

Uses bitmaps for indexing low-cardinality columns.

---

## Example

```text
gender = male/female
```

---

## Common In

* Data warehouses
* Analytics systems

---

# 15. Spatial Index

## Definition

Optimized for geographic/spatial queries.

---

## Example

```text
Find nearby restaurants
```

---

## Used In

* Maps
* GIS systems

---

# 16. Covering Index

## Definition

Index contains all columns required by query.

Meaning:

* DB can answer query using only index
* No table lookup needed

---

## Example

Query:

```sql
SELECT name FROM users WHERE email='a@test.com'
```

Index:

```text
(email, name)
```

---

# 17. Sparse vs Dense Index

---

## Dense Index

Every record has index entry.

---

## Sparse Index

Only some records indexed.

Used in clustered storage.

---

# 18. B-Tree vs Hash Index

| Feature         | B-Tree    | Hash          |
| --------------- | --------- | ------------- |
| Equality Search | Fast      | Very Fast     |
| Range Queries   | Excellent | Poor          |
| Sorting         | Supported | Not supported |
| Most Common     | Yes       | Limited       |

---

# 19. Cardinality and Indexing

## Cardinality

Number of unique values.

---

## High Cardinality

Example:

```text
email
```

Good candidate for indexing.

---

## Low Cardinality

Example:

```text
gender
```

Less useful for normal indexes.

---

# 20. Query Execution Without Index

```text
Full Table Scan
↓
Read every row
↓
Find matching rows
```

Slow for large tables.

---

# 21. Query Execution With Index

```text
Search index
↓
Find matching pointers
↓
Retrieve rows directly
```

Much faster.

---

# 22. Write Overhead of Indexes

Whenever row changes:

* Index must also update

Operations affected:

* INSERT
* UPDATE
* DELETE

---

# 23. Over-indexing Problem

Too many indexes:

* Slow writes
* Increased storage
* Maintenance overhead

---

# 24. Indexing Best Practices

* Index frequently searched columns
* Index JOIN columns
* Avoid indexing every column
* Use composite indexes carefully
* Monitor slow queries

---

# 25. Common Mistakes

---

## Indexing low-cardinality columns unnecessarily

---

## Too many indexes on write-heavy tables

---

## Wrong composite index ordering

---

## Expecting indexes to help every query

---

# 26. Real-world Examples

---

## E-commerce

Indexes on:

* product_id
* category_id
* price

---

## Social Media

Indexes on:

* user_id
* created_at
* post_id

---

## Banking

Indexes on:

* account_number
* transaction_id

---

# 27. SQL vs NoSQL Indexing

Both SQL and NoSQL systems support indexing.

Examples:

* MongoDB indexes
* Cassandra secondary indexes
* Redis indexes via modules

---

# 28. Important Mental Models

* Index = shortcut for locating data
* Faster reads, slower writes
* B-Trees optimize disk access
* Good indexing depends on query patterns

---

# 29. Interview Summary

Indexes are specialized data structures that improve query performance by reducing the amount of data scanned during retrieval operations. Common index types include primary, secondary, clustered, non-clustered, composite, and hash indexes. While indexes greatly improve read performance, they introduce storage overhead and slower write operations, making proper index design an important trade-off in database systems.

---
