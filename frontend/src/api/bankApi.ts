export type AccountType = 'checking' | 'savings' | 'cd';

export type Account = {
  type: AccountType;
  accountNumber: string;
  balance: number;
  apr: number;
  transactionHistory: string[];
  monthsPassed?: number;
  withdrawnThisMonth?: boolean;
};

export type CommandResponse = {
  valid: boolean;
  command: string;
  accounts: Account[];
  invalidCommands: string[];
};

export type BankState = {
  accounts: Account[];
  invalidCommands: string[];
};

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '';

export async function getBankState(): Promise<BankState> {
  const response = await fetch(`${API_BASE_URL}/api/state`);

  if (!response.ok) {
    throw new Error('Unable to load bank state');
  }

  return response.json();
}

export async function runCommand(command: string): Promise<CommandResponse> {
  const response = await fetch(`${API_BASE_URL}/api/commands`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ command })
  });

  const data = await response.json();
  if (!response.ok) {
    return data;
  }

  return data;
}
