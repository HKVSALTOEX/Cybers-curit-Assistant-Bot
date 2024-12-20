import discord
from discord.ext import commands

intents = discord.Intents.default()
intents.members = True

bot = commands.Bot(command_prefix='!', intents=intents)

authorized_emails = ['etudiant1@cyberschool.edu', 'etudiant2@cyberschool.edu']

@bot.event
async def on_ready():
    print(f'GateKeeper connecté en tant que {bot.user}')

@bot.event
async def on_member_join(member):
    def check(m):
        return m.author == member and isinstance(m.channel, discord.DMChannel)

    await member.send("Bienvenue à l'École de Cybersécurité ! Veuillez entrer votre adresse email pour vérification :")
    email = await bot.wait_for('message', check=check)

    if email.content in authorized_emails:
        role = discord.utils.get(member.guild.roles, name="Étudiant")
        await member.add_roles(role)
        await member.send("Vérification réussie ! Vous avez maintenant accès au serveur.")
    else:
        await member.send("Vérification échouée. Contactez un administrateur pour assistance.")

bot.run('YOUR_BOT_TOKEN')