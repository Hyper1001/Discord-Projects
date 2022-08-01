from tkinter import *
import sqlite3, csv

root = Tk()
root.title('Secret Santa Bot')

con = sqlite3.connect('test.db')

crsr = con.cursor()

# Create table
command0 = """CREATE TABLE IF NOT EXISTS
information(DISCORD_ID INTEGER PRIMARY KEY, NAME VARCHAR(50), SHIRTSIZE VARCHAR(50), SHOESIZE INTEGER NOT NULL, MUSIC VARCHAR(50),  GAME VARCHAR(50), ANIME VARCHAR(50),  MELEE VARCHAR(50),  THING VARCHAR(50),  AMAZON VARCHAR(50))"""
crsr.execute(command0)
crsr.execute("""DELETE FROM information""")

command1 = """CREATE TABLE IF NOT EXISTS
giving(GIVER_ID INTEGER NOT NULL, RECEIVER_ID INTEGER NOT NULL)"""
crsr.execute(command1)
crsr.execute("""DELETE FROM giving""")

#import data

with open('INFORMATION.csv','r') as fin:
    dr = csv.DictReader(fin)
    to_db = [(i['DISCORD_ID'], i['NAME'], i['SHIRT'], i['SHOE'], i['MUSIC'], i['GAME'], i['ANIME'], i['MELEE'], i['THING'], i['AMAZON']) for i in dr]

crsr.executemany("INSERT INTO information(DISCORD_ID, NAME, SHIRTSIZE, SHOESIZE, MUSIC, GAME, ANIME, MELEE, THING, AMAZON) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", to_db)
con.commit()
