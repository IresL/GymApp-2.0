insert into training_types(name) values
  ('fitness'), ('yoga'), ('zumba'), ('stretching'), ('resistance')
on conflict (name) do nothing;
