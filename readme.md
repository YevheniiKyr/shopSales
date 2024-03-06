Description 

This is web server that provide endpoints to get information about chain store's sales 
from MySQL database. There are both jpql and native queries 
in repositories. Overrided run method in ShopApplication is designed
to measure the duration of requests.

Tech stack

- Spring
- Hibernate
- MySQL