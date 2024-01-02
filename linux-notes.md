# Systematic Principles

Never spend 6 mins doing something by hand when you can spend 6 hours failing to automate it

- Infrastructure as Code (IaC): Focus on setup, configuration, deployment, and support of infrastructure and the applications (code as communication)
- Continuous Integration
- Continuous Deployment
- Automation: Benefits of Rapid Changes, Improved Productivity, Repeatable Configurations, Leveraged Elasticity, Leveraged auto scaling, Automated Testing
- Monitoring: Feedback comes from logs, monitoring, alerting and auditing infrastructure for co-operations
- Security: outer and inner systems such as msg encryption

# Introduction to Linux

Linux: operating system kernel (started by the boot loader [BIOS/UEFI])

Operating Systems:

- The Bootloader
- The kernel
- Daemons
- Desktop Environment
- Applications
- The Shell: a command process controlling the computer via a text interface

Users:

- human users / shell user
- system users / applicative users => limit processes' privileges

Special Characters:

- `~` tilde /ˈtildə/
- `&` ampersand /ˈæmpɚˌsænd/
- `$` dollar => a variable such as `$HOME`
- `*` asterisk /ˈastəˌrisk/
- `/` forward slash

Popular commands:

- `pwd`: print working directory
- `cd`: change (current working) directory
- `ls`: list contents of a directory
- `mkdir -p` optional, create nested directories (all non-exist files will be created)
- `rm -r` recursion /rɪ'kɝʃən/
- `ln` create hard or soft symbolic links, a hard link will map operations (like remove) to original files but the soft one not
- `cat` concatenate files and copies to std output
- Output Streams: Standard Output and Standard Error
  - Redirect Streams: `ls > std-out-and-error.log 2>&1` (save to black hole `/dev/null` if need not outputs but can not recover)
    - redirecting standard output: `>` overwrite contents, `>>` append contents
    - redirecting standard error: `2>`
- `head` print the first (ten) lines of a file but not useful
- `tail` print the last (ten) lines of a file, useful
- pipeline `cmd1 | cmd2` connect output of cmd1 with the input of cmd2
- `grep -i` sensitive search
- `chmod` change file mode, only owner and superuser
- `chown owner:group` change file owner and group, require superuser privileges
- `su [-l] user` start a shell as another user, `-l` login shell => reload working environment like working directory
- `sudo` (like `su` but different), not root but execute a cmd as another user, can be configure by admin ( like only sudo specifics cms)
- `ps` viewing processes. `ps aux | grep 'Z'` to identify zombie process
- `top` viewing processes dynamically
- `jobs` list background or suspended processes
- `fg JOB_ID` returning a process to the foreground
- `kill PID` terminate programs. `kill -9 -1` kills all processes you have permission to kill including login session
- `shutdown` power off or reboot
- `nohup` run a command without hangups. Ignores all hangup signals and redirect all outputs to `nohup.out` under the working directory when executing cmd
- `du` estimate disk usage (such as `du -sh`, `du -sm`), `-h` show each files with unit,`-s` sum of directory, `-k` show in K, `-m` show in M, `-g` show in G
- `df` report disk usage of file system (such as `df -m`)
- `tar`, `zip`,`unzip`
- `ssh user@host`
- `scp file user@host:path`
- `man` display manual page (like help in python)

Special Directory:

- `dev` for devices
- `etc` for configurations
- `opt` optional packages => usually for applications
- `var` for variable data files, always locked files like logs
- `tmp` readable and writable but not executable for all user, remove on reboot

File extensions are for human, but Linux has not concept of them

Permission Attributes (https://www.guru99.com/file-permissions.html#linux_file_permissions)

- one + nine characters, such as `-r--r--r--` => first char (`-` file /`d` directory /`i` link) + three permission chars for owner + ~ for group users + ~ for other users
- `r` openable and readable, allows a directory's contents to be listed if executable
- `w` writable and can be truncated, but not allow to be deleted and renamed unless its upper directory executable
- `x` executable, allows a directory to be entered