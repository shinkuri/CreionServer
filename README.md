# CreionServer

Client for this server: https://github.com/kekzdealer/CreionClient

With this server, I am trying to combine SQLite with an Entity-System based architecture to create a backbone for an easy to expand multiplayer RPG. 
Every object in the game world is represented by an entity. An entity is simply an UUID and traits are added to entities by attaching components that carry trait specific data.

Game logic is carried out by a number of systems running in parallel. Each of those systems operates only on a small subset of components.

Lastly, the entire list of entities and their attached components and the data that comes with them is held by a SQLite database. This allows for well structured data and quite simple usage in a multithreaded environment. It also removes the need for manual or automatic save operations as the entire world state as well as all player data is held by the database in a persistent way at any point in time.
