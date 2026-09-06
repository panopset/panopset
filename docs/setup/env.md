[home](../../README.md) ~ [stack](../stack.md) ~ [workstations](../workstations/README.md) ~ setup

[home](README.md) ~ env ~ [ssh](ssh.md) ~ [qt](qt.md)

# Environment Variables

| Name           | Description                                                                 |
|----------------|-----------------------------------------------------------------------------|
| JAVA_HOME      | Your JDK path, for example /home/linuxbrew/.linuxbrew/Cellar/openjdk/25.0.1 |
| PAN_NM         | "Your full name" in double quotes.                                          |
| PAN_EMAIL      | Your email address.                                                         |
| PAN_WS_PW      | Your workstation password.                                                  |
| PAN_SV_NM      | Your server user name.                                                      |
| PAN_SV_PW      | Your server password.                                                       |
| PAN_REDIS_HOST | Your Redis (or ValKey) URL.                                                 |
| PAN_REDIS_PORT | Your Redis port.                                                            |
| PAN_REDIS_PWD  | Your Redis password.                                                        |

## One Time Setup

Once you have added your environment
variables, run

| Linux/Mac | Windows |
| --------- | ------- |
| ./config.sh | config.cmd |

which will perform one time config tasks like setting your git username and email.

# Security Note

This document refers to workstation environment variables, which may contain personal information such as passwords 
and your name. Your workstation hard drive should be encrypted, and in a secure physical location.

---

Next, use [brew](brew.md) to install your JDK, maven, etc.
