## Solution Q1

```sql
-- noinspection SqlNoDataSourceInspectionForFile

SELECT e.id, count(*), sum(e.salary)
FROM employee e
GROUP BY e.department_id
ORDER BY e.department_id
```
