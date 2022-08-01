import discord.py
import datetime
import random
from discord.ext import commands

client = commands.Bot(command_prefix = '!')
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
async def on_member_join(member):
    greeting = client.get_channel(722171720307310612)
    UserChannel = client.get_channel(722171898963689573)
    pfp = member.avatar_url
    embed=discord.Embed(title=f"{member} has joined the server.", description=now.strftime("%d/%m/%Y %H:%M:%S"), color=0x1dff11)
    embed.set_thumbnail(url=pfp)
    await UserChannel.send(embed=embed)
    await greeting.send(f'Welcome {member.mention} to the server! Please read the <#722171572542242848> and introduce yourself in <#722180862405247049>')

@client.event
async def on_voice_state_update(member,before,after):
    if before.channel is None and after.channel is not None:
        await member.add_roles(member.guild.get_role(722172956553379911))
    if after.channel is None and before.channel is not None:
        await member.remove_roles(member.guild.get_role(722172956553379911))

@client.event
async def on_member_remove(member):
    greeting = client.get_channel(722171720307310612)
    UserChannel = client.get_channel(722171898963689573)
    pfp = member.avatar_url
    embed=discord.Embed(title=f"{member} has left the server.", description=now.strftime("%d/%m/%Y %H:%M:%S"),color=0xedff0d)
    embed.set_thumbnail(url=pfp)
    await UserChannel.send(embed=embed)
    await greeting.send(f'{member} has left the server.')

@client.event
async def on_message_delete(message):
    MessageChannel = client.get_channel(722171914893787148)
    embed=discord.Embed(title=f"{message.author}'s message has been deleted from #{message.channel}.", description=now.strftime("%d/%m/%Y %H:%M:%S"), color=0xff0d0d)
    pfp = message.author.avatar_url
    embed.set_thumbnail(url=pfp)
    embed.add_field(name='Deleted Message', value=f'{message.content}', inline=False)
    await MessageChannel.send(embed=embed)

@client.event
async def on_message_edit(before,after):
    MessageChannel = client.get_channel(722171914893787148)
    if before.content == after.content:
        return
    embed=discord.Embed(title=f"{before.author}'s message has been edited in {before.channel}.", description=now.strftime("%d/%m/%Y %H:%M:%S"), color=0xedff0d)
    pfp = before.author.avatar_url
    embed.set_thumbnail(url=pfp)
    embed.add_field(name='Old Message', value=f'{before.content}', inline=False)
    embed.add_field(name='New Message', value=f'{after.content}', inline=False)
    await MessageChannel.send(embed=embed)

@client.command()
@commands.check(is_admin)
async def kick(ctx, member : discord.Member,*,reason=None):
    await ctx.send(f'{member} has been kicked.')
    UserChannel = client.get_channel(722172115255558165)
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
    UserChannel = client.get_channel(722172115255558165)
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

@client.command()
async def ping(ctx):
    await ctx.send(f'Pong! {round(client.latency * 1000)}ms')

client.run('NzIyMTcwNTkxNDczMzAzNTkz.XufSnQ.aXNW5BfBpCvRCzmhMMF2VY6hTtw')
