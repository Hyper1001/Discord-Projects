import discord
import datetime
import random
import copy
from aiohttp import request
from discord.ext import commands
from discord.utils import get
from random import *
from tkinter import *
import sqlite3, csv

root = Tk()
root.title('Secret Santa Bot')

con = sqlite3.connect('test.db')

crsr = con.cursor()

intents = discord.Intents.default()
intents.members = True

client = commands.Bot(command_prefix = '/', intents = intents)
now = datetime.datetime.now()

async def is_admin(ctx):
    try:
        if ctx.author.permissions_in(ctx.message.channel).administrator:
            return True
        await ctx.send("No Admin permissions were found. <@188485629485514752> will be notified.")
        return False
    except Exception as e:
        print(e)
        return False

@client.event
async def on_ready():
    print('Bot is ready.')

@client.event
async def on_raw_reaction_add(payload):
    if payload.message_id == 996336889625837638:
        print(payload.member.name + " added reaction " + payload.emoji.name)
        role = payload.emoji.name[4:]
        print(role)
        await payload.member.add_roles(get(client.get_guild(payload.guild_id).roles, name = role))

@client.event
async def on_raw_reaction_remove(payload):
    if payload.message_id == 996336889625837638:
        payload.member = client.get_guild(payload.guild_id).get_member(payload.user_id)
        print(payload.member.name + " removed reaction " + payload.emoji.name)
        role = payload.emoji.name[4:]
        print(role)
        await payload.member.remove_roles(get(client.get_guild(payload.guild_id).roles, name = role))

@client.event
async def on_member_join(member):
    greeting = client.get_channel(827455384532549662)
    UserChannel = client.get_channel(827455439649505290)
    pfp = member.avatar_url
    print(f'{member} has joined the server')
    embed=discord.Embed(title=f"{member} has joined the server.", description=now.strftime("%m/%d/%Y %H:%M:%S"), color=0x1dff11)
    embed.set_thumbnail(url=pfp)
    await UserChannel.send(embed=embed)
    await greeting.send(f'Welcome test to the Dolphin Tank! Please read the #rules and introduce yourself in this channel. If a colour role is needed please ask one of the mods (except Cornsplosion) to assign you one. Have fun!')

@client.event
async def on_member_remove(member):
    greeting = client.get_channel(827455384532549662)
    UserChannel = client.get_channel(827455439649505290)
    pfp = member.avatar_url
    print(f'{member} has left the server')
    embed=discord.Embed(title=f"{member} has left the server.", description=now.strftime("%m/%d/%Y %H:%M:%S"),color=0xedff0d)
    embed.set_thumbnail(url=pfp)
    await UserChannel.send(embed=embed)
    await greeting.send(f'{member} has left the server.')

@client.event
async def on_message_delete(message):
    MessageChannel = client.get_channel(827455490681208842)
    embed=discord.Embed(title=f"{message.author}'s message has been deleted from #{message.channel}.", description=now.strftime("%m/%d/%Y %H:%M:%S"), color=0xff0d0d)
    pfp = message.author.avatar_url
    embed.set_thumbnail(url=pfp)
    embed.add_field(name='Deleted Message', value=f'{message.content}', inline=False)
    await MessageChannel.send(embed=embed)

@client.event
async def on_message_edit(before,after):
    MessageChannel = client.get_channel(827455490681208842)
    if before.content == after.content:
        return
    embed=discord.Embed(title=f"{before.author}'s message has been edited in {before.channel}.", description=now.strftime("%m/%d/%Y %H:%M:%S"), color=0xedff0d)
    pfp = before.author.avatar_url
    embed.set_thumbnail(url=pfp)
    embed.add_field(name='Old Message', value=f'{before.content}', inline=False)
    embed.add_field(name='New Message', value=f'{after.content}', inline=False)
    await MessageChannel.send(embed=embed)

@client.command()
async def ping(ctx):
    await ctx.send(f'pong! {round(client.latency * 1000)}ms')

@client.command()
async def fact(ctx):
    async with request("GET","https://some-random-api.ml/facts/dog",headers = {}) as response:
        if response.status==200:
            data = await response.json()
            await ctx.send(data["fact"])
        else:
            await ctx.send("API returned a {response.status} status")

@client.command()
async def msg(ctx, member: discord.Member,*,content):
    channel = await member.create_dm()
    await channel.send(content)

@client.command()
async def helps(ctx):
    embed=discord.Embed(title="Command Information", color=0x66ff00)
    embed.add_field(name="/profile", value="Looks at your profile", inline=False)
    embed.add_field(name="/santaprofile", value="Looks at your Secret Santa's profile", inline=False)
    embed.add_field(name="/ask (message)", value="Ask your gift receiver a question anonymously.", inline=False)
    embed.add_field(name="/respond (message)", value="Respond to your Secret Santa.", inline=False)
    embed.add_field(name="/update (category) (new information)", value="Updates your profile and sends new information to your Secret Santa.", inline=False)
    embed.add_field(name="/profcheckeveryone", value="(ADMIN ONLY) Sends every participant their own profile to make sure that it is correct before rolling.", inline=False)
    embed.add_field(name="/roll", value="(ADMIN ONLY) Shuffles participants and assigns them to be a Secret Santa for another participant.", inline=False)
    embed.add_field(name="/helps", value="Brings up this embed.", inline=False)
    embed.set_footer(text="Please contact @Hyper#5957 if you encounter any issues.")
    await client.get_user(ctx.author.id).send(embed=embed)

@client.command()
async def profcheckeveryone(ctx):
    crsr.execute("""SELECT * from information""") #get all participant information
    id = crsr.fetchall()
    iterator = 0
    for x in id:
        embed=discord.Embed(title="Your Profile", description="   ", color=0x00b34d)
        embed.set_thumbnail(url="https://cdn.iconscout.com/icon/premium/png-256-thumb/santa-claus-2993809-2487115.png")
        embed.add_field(name="Name", value=crsr.execute("SELECT NAME from information").fetchall()[iterator][0], inline=False)
        embed.add_field(name="Shirt Size", value=crsr.execute("SELECT SHIRTSIZE from information").fetchall()[iterator][0], inline=True)
        embed.add_field(name="Shoe Size", value=crsr.execute("SELECT SHOESIZE from information").fetchall()[iterator][0], inline=True)
        embed.add_field(name="Favorite Music", value=crsr.execute("SELECT MUSIC from information").fetchall()[iterator][0], inline=False)
        embed.add_field(name="Favorite Games", value=crsr.execute("SELECT GAME from information").fetchall()[iterator][0], inline=False)
        embed.add_field(name="Favorite Anime", value=crsr.execute("SELECT ANIME from information").fetchall()[iterator][0], inline=False)
        embed.add_field(name="Favorite Melee Weapon", value=crsr.execute("SELECT MELEE from information").fetchall()[iterator][0], inline=False)
        embed.add_field(name="Favorite Thing", value=crsr.execute("SELECT THING from information").fetchall()[iterator][0], inline=False)
        embed.add_field(name="Amazon Wishlist", value=crsr.execute("SELECT AMAZON from information").fetchall()[iterator][0], inline=False)
        user = client.get_user(crsr.execute("SELECT DISCORD_ID from information").fetchall()[iterator][0])
        await user.send(embed=embed)
        iterator+=1

@client.command()
async def profile(ctx): #not completely implemented
    userID = crsr.execute("""SELECT DISCORD_ID from information WHERE DISCORD_ID == """+str(ctx.author.id)).fetchall()[0][0]
    embed=discord.Embed(title="Your Profile", description="   ", color=0x00b34d)
    embed.set_thumbnail(url="https://cdn.iconscout.com/icon/premium/png-256-thumb/santa-claus-2993809-2487115.png")
    embed.add_field(name="Name", value=crsr.execute("SELECT NAME from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Shirt Size", value=crsr.execute("SELECT SHIRTSIZE from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=True)
    embed.add_field(name="Shoe Size", value=crsr.execute("SELECT SHOESIZE from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=True)
    embed.add_field(name="Favorite Music", value=crsr.execute("SELECT MUSIC from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Games", value=crsr.execute("SELECT GAME from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Anime", value=crsr.execute("SELECT ANIME from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Melee Weapon", value=crsr.execute("SELECT MELEE from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Thing", value=crsr.execute("SELECT THING from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Amazon Wishlist", value=crsr.execute("SELECT AMAZON from information WHERE DISCORD_ID == " + str(userID)).fetchall()[0][0], inline=False)
    await client.get_user(ctx.author.id).send(embed=embed)

@client.command()
async def santaprofile(ctx):
    recieverID = crsr.execute("SELECT RECEIVER_ID from giving WHERE GIVER_ID == " + str(ctx.author.id)).fetchall()[0][0]
    embed=discord.Embed(title="Your Secret Santa's profile", description="   ", color=0x00b34d)
    embed.set_thumbnail(url="https://cdn.iconscout.com/icon/premium/png-256-thumb/santa-claus-2993809-2487115.png")
    embed.add_field(name="Name", value=crsr.execute("SELECT NAME from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Shirt Size", value=crsr.execute("SELECT SHIRTSIZE from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=True)
    embed.add_field(name="Shoe Size", value=crsr.execute("SELECT SHOESIZE from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=True)
    embed.add_field(name="Favorite Music", value=crsr.execute("SELECT MUSIC from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Games", value=crsr.execute("SELECT GAME from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Anime", value=crsr.execute("SELECT ANIME from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Melee Weapon", value=crsr.execute("SELECT MELEE from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Favorite Thing", value=crsr.execute("SELECT THING from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=False)
    embed.add_field(name="Amazon Wishlist", value=crsr.execute("SELECT AMAZON from information WHERE DISCORD_ID == " + str(recieverID)).fetchall()[0][0], inline=False)
    await client.get_user(ctx.author.id).send(embed=embed)

@client.command()
async def roll(ctx):
    crsr.execute("""SELECT DISCORD_ID from information""")
    id = crsr.fetchall()
    shuffle(id)
    offset = [id[-1]] + id[:-1]
    x = list(zip(id,offset))
    for santa, receiver in x:
        crsr.execute("INSERT into giving VALUES("+str(santa[0])+","+str(receiver[0])+")")
        con.commit()
        user = client.get_user(santa[0])
        embed=discord.Embed(title="Your Secret Santa is...", description="   ", color=0x00b34d)
        embed.set_thumbnail(url="https://cdn.iconscout.com/icon/premium/png-256-thumb/santa-claus-2993809-2487115.png")
        embed.add_field(name="Name", value=crsr.execute("SELECT NAME from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=False)
        embed.add_field(name="Shirt Size", value=crsr.execute("SELECT SHIRTSIZE from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=True)
        embed.add_field(name="Shoe Size", value=crsr.execute("SELECT SHOESIZE from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=True)
        embed.add_field(name="Favorite Music", value=crsr.execute("SELECT MUSIC from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Games", value=crsr.execute("SELECT GAME from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Anime", value=crsr.execute("SELECT ANIME from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Melee Weapon", value=crsr.execute("SELECT MELEE from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Thing", value=crsr.execute("SELECT THING from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=False)
        embed.add_field(name="Amazon Wishlist", value=crsr.execute("SELECT AMAZON from information WHERE DISCORD_ID == "+str(receiver[0])).fetchall()[0][0], inline=False)
        await user.send(embed=embed)

@client.command()
async def update(ctx,category,*,message):
    category = category.upper()
    if category == "NAME" or category == "SHIRTSIZE" or category == "SHOESIZE" or category == "MUSIC" or category == "GAMES" or category == "ANIME" or category == "MELEE" or category == "THING" or category == "AMAZON":
        gifterID = crsr.execute("SELECT GIVER_ID from giving WHERE RECEIVER_ID =="+str(ctx.author.id)).fetchall()[0][0]
        await client.get_user(gifterID).send("Your receiver has recently updated their " + category.lower() + " to\n" + message)
        crsr.execute("UPDATE information SET " + category + " = \"" + str(message) + "\" WHERE DISCORD_ID == " + str(ctx.author.id))
        con.commit()
        embed=discord.Embed(title="Your Profile", description="   ", color=0x00b34d)
        embed.set_thumbnail(url="https://cdn.iconscout.com/icon/premium/png-256-thumb/santa-claus-2993809-2487115.png")
        embed.add_field(name="Name", value=crsr.execute("SELECT NAME from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=False)
        embed.add_field(name="Shirt Size", value=crsr.execute("SELECT SHIRTSIZE from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=True)
        embed.add_field(name="Shoe Size", value=crsr.execute("SELECT SHOESIZE from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=True)
        embed.add_field(name="Favorite Music", value=crsr.execute("SELECT MUSIC from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Games", value=crsr.execute("SELECT GAME from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Anime", value=crsr.execute("SELECT ANIME from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Melee Weapon", value=crsr.execute("SELECT MELEE from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=False)
        embed.add_field(name="Favorite Thing", value=crsr.execute("SELECT THING from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=False)
        embed.add_field(name="Amazon Wishlist", value=crsr.execute("SELECT AMAZON from information WHERE DISCORD_ID == "+str(ctx.author.id)).fetchall()[0][0], inline=False)
        await client.get_user(gifterID).send(embed=embed)
        await client.get_user(ctx.author.id).send("Update completed! Your Secret Santa has been notified of your new changes.")
    else:
        await client.get_user(ctx.author.id).send("Not a valid category, please retry with one of the following categories (Name, Shirt, Shoe, Music, Games, Anime, Melee, Thing, Amazon)")

@client.command()
async def ask(ctx,*,content):
    giver = ctx.author.id
    receiver = client.get_user(crsr.execute("SELECT RECEIVER_ID from giving WHERE GIVER_ID == "+str(giver)).fetchall()[0][0])
    embed=discord.Embed(title=":incoming_envelope:You have received a question from your Secret Santa:incoming_envelope:")
    embed.set_thumbnail(url="https://cdn1.iconfinder.com/data/icons/christmas-and-new-year-23/64/Christmas_Santa_Claus-12-512.png")
    embed.add_field(name="Question", value=content, inline=False)
    embed.set_footer(text="Please respond to your Secret Santa by using\n/respond {response here}")
    await receiver.send(embed=embed)
    await client.get_user(giver).send(":incoming_envelope:Your Question has been sent!:incoming_envelope:")

@client.command()
async def respond(ctx,*,content):
    receiver = ctx.author.id
    if(content==None):
        content = "(blank)"
    giver = client.get_user(crsr.execute("SELECT GIVER_ID from giving WHERE RECEIVER_ID =="+str(receiver)).fetchall()[0][0])
    embed=discord.Embed(title=":incoming_envelope:You have received a response from your Receiver:incoming_envelope:")
    embed.set_thumbnail(url="https://cdn1.iconfinder.com/data/icons/aami-flat-emails/64/email-41-512.png")
    embed.add_field(name="Question", value=content, inline=False)
    embed.set_footer(text="If you have more questions for your secret santa, please use\n/ask {question here}")
    await giver.send(embed=embed)
    await client.get_user(receiver).send(":incoming_envelope:Your Response has been sent!:incoming_envelope:")

# @client.command()
# async def roll(ctx):
#     i = 1;
#     x=[]
#     while i>0:
#         shuffle(list_names)
#         offset = [list_names[-1]] + list_names[:-1]
#         x = list(zip(list_names,offset))
#         i = 0
#         for santa,receiver in x:
#             if (santa.name == "Quinten Robb" or santa.name == "Dylan Trang" or santa.name == "Jared Carbary") and (receiver.name == "Tim Tran" or receiver.name == "Peter Phan"or receiver.name == "Analys Kiv" or receiver.name == "Megan Meza" or receiver.name == "Nathan Nguyen" or receiver.name == "Zach Jennings" or receiver.name == "Kenny Ho"):
#                 i = 1
#             elif (santa.name == "Tim Tran" or santa.name == "Peter Phan" or santa.name == "Analys Kiv" or santa.name == "Megan Meza" or santa.name == "Nathan Nguyen" or santa.name == "Zach Jennings" or santa.name == "Kenny Ho") and (receiver.name == "Quinten Robb" or receiver.name == "Dylan Trang" or receiver.name == "Jared Carbary"):
#                 i = 1
#
#     for santa, receiver in x:
#         user = client.get_user(int(santa.tag))
#         embed=discord.Embed(title="Your Secret Santa is...", description="   ", color=0x00b34d)
#         embed.set_thumbnail(url="https://cdn.iconscout.com/icon/premium/png-256-thumb/santa-claus-2993809-2487115.png")
#         embed.add_field(name="Name", value=receiver.name, inline=False)
#         embed.add_field(name="Shirt Size", value=receiver.shirtSize, inline=True)
#         embed.add_field(name="Shoe Size", value=receiver.shoeSize, inline=True)
#         embed.add_field(name="Favorite Music", value=receiver.favMusic, inline=False)
#         embed.add_field(name="Favorite Games", value=receiver.favGame, inline=False)
#         embed.add_field(name="Favorite Anime", value=receiver.favAnime, inline=False)
#         embed.add_field(name="Favorite Melee Weapon", value=receiver.favMelee, inline=False)
#         embed.add_field(name="Favorite Thing", value=receiver.favThing, inline=False)
#         embed.add_field(name="Amazon Wishlist", value=receiver.amazon, inline=False)
#         await user.send(embed=embed)

@client.command()
@commands.check(is_admin)
async def kick(ctx, member : discord.Member,*,reason=None):
    await ctx.send(f'{member} has been kicked.')
    UserChannel = client.get_channel(827455439649505290)
    pfp = member.avatar_url
    embed=discord.Embed(title=f"{member} has been kicked from the server.", description=now.strftime("%d/%m/%Y %H:%M:%S"),color=0xedff0d)
    embed.set_thumbnail(url=pfp)
    embed.add_field(name='Reason',value=f'{reason}',inline=False)
    await UserChannel.send(embed=embed)
    await member.kick(reason=reason)

@client.command()
@commands.check(is_admin)
async def ban(ctx, member : discord.Member, *, reason=None):
    await ctx.send(f'{member} has been banned.')
    UserChannel = client.get_channel(827455439649505290)
    pfp = member.avatar_url
    embed=discord.Embed(title=f"{member} has been banned from the server.", description=now.strftime("%d/%m/%Y %H:%M:%S"),color=0xff0d0d)
    embed.set_thumbnail(url=pfp)
    embed.add_field(name='Reason',value=f'{reason}',inline=False)
    await UserChannel.send(embed=embed)
    await member.ban(reason=reason)

@client.command()
@commands.check(is_admin)
async def send(ctx, channel: discord.TextChannel,*,msg):
    sent = client.get_channel(channel.id)
    await sent.send(f'{msg}')

client.run('ODI3NDUzNzYyMDcwNzczNzgx.YGbQXg.fDZI_yZxyvsGXZoVl6Av-PRdZdQ')
