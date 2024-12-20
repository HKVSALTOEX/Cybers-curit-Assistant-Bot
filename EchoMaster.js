const { Client, GatewayIntentBits } = require('discord.js');
const client = new Client({ intents: [GatewayIntentBits.Guilds] });

client.on('ready', () => {
    console.log(`EchoMaster connecté en tant que ${client.user.tag}`);
});

client.on('messageCreate', async message => {
    if (message.content.startsWith('!annonce') && message.member.roles.cache.some(role => role.name === 'Enseignant')) {
        const channel = message.guild.channels.cache.find(ch => ch.name === 'annonces-importantes');
        if (channel) {
            const announcement = message.content.replace('!annonce', '').trim();
            channel.send(announcement);
        }
    }
});

client.login('YOUR_BOT_TOKEN');