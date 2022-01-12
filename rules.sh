# # Reject spoofed packets
# sudo iptables -A INPUT -s 10.0.0.0/8 -j DROP
# sudo iptables -A INPUT -s 169.254.0.0/16 -j DROP
# sudo iptables -A INPUT -s 172.16.0.0/12 -j DROP
# sudo iptables -A INPUT -i eth0 -s 127.0.0.0/8 -j DROP

# sudo iptables -A INPUT -s 224.0.0.0/4 -j DROP
# sudo iptables -A INPUT -d 224.0.0.0/4 -j DROP
# sudo iptables -A INPUT -s 240.0.0.0/5 -j DROP
# sudo iptables -A INPUT -d 240.0.0.0/5 -j DROP
# sudo iptables -A INPUT -s 0.0.0.0/8 -j DROP
# sudo iptables -A INPUT -d 0.0.0.0/8 -j DROP
# sudo iptables -A INPUT -d 239.255.255.0/24 -j DROP
# sudo iptables -A INPUT -d 255.255.255.255 -j DROP

# # Stop smurf attacks
# sudo iptables -A INPUT -p icmp -m icmp --icmp-type address-mask-request -j DROP
# sudo iptables -A INPUT -p icmp -m icmp --icmp-type timestamp-request -j DROP

# # Drop all invalid packets
# sudo iptables -A INPUT -m state --state INVALID -j DROP
# sudo iptables -A FORWARD -m state --state INVALID -j DROP
# sudo iptables -A OUTPUT -m state --state INVALID -j DROP

# # Drop excessive RST packets to avoid smurf attacks
# sudo iptables -A INPUT -p tcp -m tcp --tcp-flags RST RST -m limit --limit 2/second --limit-burst 2 -j ACCEPT

# # Attempt to block portscans
# # Anyone who tried to portscan us is locked out for an entire day.
# sudo iptables -A INPUT   -m recent --name portscan --rcheck --seconds 86400 -j DROP
# sudo iptables -A FORWARD -m recent --name portscan --rcheck --seconds 86400 -j DROP

# # Once the day has passed, remove them from the portscan list
# sudo iptables -A INPUT   -m recent --name portscan --remove
# sudo iptables -A FORWARD -m recent --name portscan --remove

# # These rules add scanners to the portscan list, and log the attempt.
# sudo iptables -A INPUT   -p tcp -m tcp --dport 139 -m recent --name portscan --set -j LOG --log-prefix "Portscan:"
# sudo iptables -A INPUT   -p tcp -m tcp --dport 139 -m recent --name portscan --set -j DROP

# sudo iptables -A FORWARD -p tcp -m tcp --dport 139 -m recent --name portscan --set -j LOG --log-prefix "Portscan:"
# sudo iptables -A FORWARD -p tcp -m tcp --dport 139 -m recent --name portscan --set -j DROP

sudo iptables -A INPUT -i eth0 -p tcp -m tcp --dport 14444 -m state --state NEW -m hashlimit --hashlimit-above 10/sec --hashlimit-burst 20 --hashlimit-mode srcip --hashlimit-name http_rate -j DROP
sudo iptables -A INPUT -i eth0 -p tcp -m tcp --dport 14444 -m hashlimit --hashlimit-above 32kb/s --hashlimit-mode srcip --hashlimit-name http_bandwidth -j DROP
sudo iptables -A INPUT -i eth0 -p tcp -m tcp --dport 14444 -m connlimit --connlimit-above 40 --connlimit-mask 32 --connlimit-saddr -j REJECT --reject-with tcp-reset
