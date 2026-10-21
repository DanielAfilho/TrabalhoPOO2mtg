INSERT INTO Tipo (nomeTipo) VALUES
 ('Artefato'),
 ('Criatura'),
 ('Encantamento'),
 ('Planeswalker'),
 ('Terreno')
ON CONFLICT DO NOTHING;

INSERT INTO Supertipo (nomeSupertipo) VALUES
 ('Básico'),
 ('Lendário')
ON CONFLICT DO NOTHING;

INSERT INTO Subtipo (nomeSubtipo) VALUES
 ('Aura'),
 ('Badger'),
 ('Bear'),
 ('Cat'),
 ('Class'),
 ('Construct'),
 ('Crab'),
 ('Curse'),
 ('Doctor'),
 ('Druid'),
 ('Elemental'),
 ('Elf'),
 ('Equipment'),
 ('Fish'),
 ('Forest'),
 ('Frog'),
 ('Horror'),
 ('Human'),
 ('Island'),
 ('Lizard'),
 ('Mutant'),
 ('Ooze'),
 ('Pirate'),
 ('Rabbit'),
 ('Raccoon'),
 ('Ranger'),
 ('Saga'),
 ('Samurai'),
 ('Satyr'),
 ('Scout'),
 ('Shaman'),
 ('Shapeshifter'),
 ('Siren'),
 ('Snake'),
 ('Spirit'),
 ('Tamiyo'),
 ('Time Lord'),
 ('Treefolk'),
 ('Troll'),
 ('Vehicle'),
 ('Warrior'),
 ('Wizard')
ON CONFLICT DO NOTHING;

INSERT INTO Cor (nomeCor) VALUES
 ('Azul'),
 ('Incolor'),
 ('Verde')
ON CONFLICT DO NOTHING;

