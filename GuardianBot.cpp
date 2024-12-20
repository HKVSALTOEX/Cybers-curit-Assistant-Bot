#include <iostream>
#include <fstream>
#include <string>
#include <discordpp/bot.hh>
#include <openssl/md5.h>

using namespace discordpp;

class GuardianBot : public Bot {
public:
    GuardianBot(const std::string &token) {
        this->token = token;
    }

    void onMessage(Message message) {
        if (message.content.substr(0, 6) == "!clear") {
            clearMessages(message);
        } else if (message.content.substr(0, 11) == "!verifyfile") {
            verifyFile(message);
        }
    }

    void clearMessages(Message message) {
        // Implémentation de la suppression des messages
    }

    void verifyFile(Message message) {
        std::string file_path = message.content.substr(12);
        std::ifstream file(file_path, std::ifstream::binary);

        if (!file) {
            sendMessage(message.channel_id, "Erreur : Fichier introuvable.");
            return;
        }

        MD5_CTX md5Context;
        MD5_Init(&md5Context);

        char buffer[1024];
        while (file.read(buffer, sizeof(buffer))) {
            MD5_Update(&md5Context, buffer, file.gcount());
        }

        unsigned char result[MD5_DIGEST_LENGTH];
        MD5_Final(result, &md5Context);

        std::ostringstream hash;
        for (int i = 0; i < MD5_DIGEST_LENGTH; ++i) {
            hash << std::hex << std::setw(2) << std::setfill('0') << (int)result[i];
        }

        sendMessage(message.channel_id, "Vérification réussie. Hash du fichier : " + hash.str());
    }
};

int main() {
    GuardianBot bot("YOUR_BOT_TOKEN");
    bot.run();
    return 0;
}