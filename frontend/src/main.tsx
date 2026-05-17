import React, { FormEvent, useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import {
  ArrowRightLeft,
  BadgeDollarSign,
  Banknote,
  CalendarClock,
  CircleDollarSign,
  Info,
  Landmark,
  Plus,
  ReceiptText,
  Send,
  Wallet
} from 'lucide-react';
import { Account, AccountType, getBankState, runCommand } from './api/bankApi';
import './styles.css';

type CommandResult = {
  command: string;
  valid: boolean;
};

type MoneyAction = 'deposit' | 'withdraw' | 'transfer';

function App() {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [invalidCommands, setInvalidCommands] = useState<string[]>([]);
  const [commandResults, setCommandResults] = useState<CommandResult[]>([]);
  const [customCommand, setCustomCommand] = useState('');
  const [status, setStatus] = useState('Ready');

  useEffect(() => {
    getBankState()
      .then((state) => {
        setAccounts(state.accounts);
        setInvalidCommands(state.invalidCommands);
      })
      .catch(() => setStatus('Start the Java API on port 8080 to connect the dashboard.'));
  }, []);

  const totals = useMemo(() => {
    const balance = accounts.reduce((sum, account) => sum + account.balance, 0);
    return {
      accounts: accounts.length,
      balance
    };
  }, [accounts]);

  async function submitCommand(command: string) {
    const trimmed = command.trim();
    if (!trimmed) {
      return;
    }

    setStatus('Sending command...');
    try {
      const result = await runCommand(trimmed);
      setAccounts(result.accounts);
      setInvalidCommands(result.invalidCommands);
      setCommandResults((current) => [{ command: trimmed, valid: result.valid }, ...current].slice(0, 12));
      setStatus(result.valid ? 'Command accepted' : 'Command rejected by the Java validators');
    } catch {
      setStatus('Could not reach the Java API. Check that BankHttpServer is running.');
    }
  }

  function handleCustomCommand(event: FormEvent) {
    event.preventDefault();
    submitCommand(customCommand);
    setCustomCommand('');
  }

  return (
    <main>
      <section className="topbar">
        <div>
          <p className="eyebrow">Java Bank System</p>
          <h1>Bank Operations Dashboard</h1>
        </div>
        <div className="status">{status}</div>
      </section>

      <section className="stats-grid">
        <Metric icon={<Landmark />} label="Accounts" value={totals.accounts.toString()} />
        <Metric icon={<CircleDollarSign />} label="Total balance" value={formatMoney(totals.balance)} />
        <Metric icon={<ReceiptText />} label="Invalid commands" value={invalidCommands.length.toString()} />
      </section>

      <section className="workbench">
        <div className="panel">
          <div className="panel-heading">
            <Plus />
            <h2>Create account</h2>
          </div>
          <CreateAccountForm onSubmit={submitCommand} />
        </div>

        <div className="panel">
          <div className="panel-heading">
            <BadgeDollarSign />
            <h2>Move money</h2>
          </div>
          <MoneyForms accounts={accounts} onSubmit={submitCommand} />
        </div>

        <div className="panel">
          <div className="panel-heading">
            <CalendarClock />
            <h2>Pass time</h2>
          </div>
          <PassTimeForm onSubmit={submitCommand} />
        </div>

        <form className="panel command-panel" onSubmit={handleCustomCommand}>
          <div className="panel-heading">
            <Send />
            <h2>Raw command</h2>
          </div>
          <div className="inline-form">
            <input
              value={customCommand}
              onChange={(event) => setCustomCommand(event.target.value)}
              placeholder="Deposit 12345678 500"
            />
            <button aria-label="Run command" title="Run command" type="submit">
              <Send />
            </button>
          </div>
        </form>
      </section>

      <RulesPanel />

      <section className="content-grid">
        <div className="accounts">
          <div className="section-heading">
            <Wallet />
            <h2>Accounts</h2>
          </div>
          {accounts.length === 0 ? (
            <div className="empty-state">Create an account to begin.</div>
          ) : (
            <div className="account-grid">
              {accounts.map((account) => (
                <AccountCard account={account} key={account.accountNumber} />
              ))}
            </div>
          )}
        </div>

        <aside className="activity">
          <div className="section-heading">
            <ReceiptText />
            <h2>Activity</h2>
          </div>
          <CommandList results={commandResults} invalidCommands={invalidCommands} />
        </aside>
      </section>
    </main>
  );
}

function RulesPanel() {
  return (
    <section className="rules-panel">
      <div className="section-heading">
        <Info />
        <h2>Banking rules</h2>
      </div>
      <div className="rules-grid">
        <RuleBlock
          title="Create accounts"
          items={[
            'Account numbers must be exactly 8 digits.',
            'APR must be between 0 and 10.',
            'Checking and savings start at $0.',
            'CD accounts require a starting balance from $1,000 to $10,000.'
          ]}
        />
        <RuleBlock
          title="Deposits"
          items={[
            'Checking deposits can be up to $1,000.',
            'Savings deposits can be up to $2,500.',
            'CD accounts cannot receive deposits after creation.',
            'Deposit amounts cannot be negative.'
          ]}
        />
        <RuleBlock
          title="Withdrawals"
          items={[
            'Checking withdrawals can be up to $400.',
            'Savings withdrawals can be up to $1,000 and only once per month.',
            'CD withdrawals are allowed only after 12 months.',
            'A CD withdrawal must withdraw the full balance.'
          ]}
        />
        <RuleBlock
          title="Transfers and time"
          items={[
            'Transfers only work between checking and savings accounts.',
            'CD accounts cannot send or receive transfers.',
            'Transfer amounts follow the withdrawal rule for the sender and deposit rule for the receiver.',
            'Passing time accepts 0 to 60 months and applies fees, interest, and monthly resets.'
          ]}
        />
      </div>
      <div className="examples">
        <strong>Command examples</strong>
        <code>Create savings 12345678 0.6</code>
        <code>Deposit 12345678 700</code>
        <code>Transfer 12345678 87654321 300</code>
        <code>Pass 1</code>
      </div>
    </section>
  );
}

function RuleBlock({ title, items }: { title: string; items: string[] }) {
  return (
    <article className="rule-block">
      <h3>{title}</h3>
      <ul>
        {items.map((item) => (
          <li key={item}>{item}</li>
        ))}
      </ul>
    </article>
  );
}

function Metric({ icon, label, value }: { icon: React.ReactNode; label: string; value: string }) {
  return (
    <div className="metric">
      {icon}
      <div>
        <span>{label}</span>
        <strong>{value}</strong>
      </div>
    </div>
  );
}

function CreateAccountForm({ onSubmit }: { onSubmit: (command: string) => void }) {
  const [type, setType] = useState<AccountType>('checking');
  const [accountNumber, setAccountNumber] = useState('');
  const [apr, setApr] = useState('');
  const [balance, setBalance] = useState('');

  function handleSubmit(event: FormEvent) {
    event.preventDefault();
    const command = type === 'cd'
      ? `Create cd ${accountNumber} ${apr} ${balance}`
      : `Create ${type} ${accountNumber} ${apr}`;
    onSubmit(command);
  }

  return (
    <form className="stack-form" onSubmit={handleSubmit}>
      <div className="segmented">
        {(['checking', 'savings', 'cd'] as AccountType[]).map((option) => (
          <button
            className={type === option ? 'selected' : ''}
            key={option}
            onClick={() => setType(option)}
            type="button"
          >
            {option}
          </button>
        ))}
      </div>
      <label>
        Account number
        <input
          maxLength={8}
          minLength={8}
          onChange={(event) => setAccountNumber(event.target.value)}
          pattern="\d{8}"
          placeholder="12345678"
          value={accountNumber}
        />
      </label>
      <label>
        APR
        <input
          max="10"
          min="0"
          onChange={(event) => setApr(event.target.value)}
          placeholder="0.6"
          step="0.01"
          type="number"
          value={apr}
        />
      </label>
      {type === 'cd' && (
        <label>
          Starting balance
          <input
            max="10000"
            min="1000"
            onChange={(event) => setBalance(event.target.value)}
            placeholder="2000"
            step="0.01"
            type="number"
            value={balance}
          />
        </label>
      )}
      <button className="primary" type="submit">
        <Plus />
        Create
      </button>
    </form>
  );
}

function MoneyForms({ accounts, onSubmit }: { accounts: Account[]; onSubmit: (command: string) => void }) {
  const [action, setAction] = useState<MoneyAction>('deposit');
  const [accountNumber, setAccountNumber] = useState('');
  const [toAccountNumber, setToAccountNumber] = useState('');
  const [amount, setAmount] = useState('');
  const selectableAccounts = accounts.map((account) => account.accountNumber);
  const actionLabel = action.charAt(0).toUpperCase() + action.slice(1);

  function selected(label: string, value: string, setter: (value: string) => void) {
    return (
      <label>
        {label}
        <select onChange={(event) => setter(event.target.value)} value={value}>
          <option value="">Choose account</option>
          {selectableAccounts.map((number) => (
            <option key={number} value={number}>
              {number}
            </option>
          ))}
        </select>
      </label>
    );
  }

  function handleSubmit(event: FormEvent) {
    event.preventDefault();
    const command = action === 'transfer'
      ? `Transfer ${accountNumber} ${toAccountNumber} ${amount}`
      : `${actionLabel} ${accountNumber} ${amount}`;
    onSubmit(command);
  }

  return (
    <div className="money-panel">
      <div className="segmented money-mode">
        <button
          className={action === 'deposit' ? 'selected' : ''}
          onClick={() => setAction('deposit')}
          type="button"
        >
          <Banknote />
          Deposit
        </button>
        <button
          className={action === 'withdraw' ? 'selected' : ''}
          onClick={() => setAction('withdraw')}
          type="button"
        >
          <Wallet />
          Withdraw
        </button>
        <button
          className={action === 'transfer' ? 'selected' : ''}
          onClick={() => setAction('transfer')}
          type="button"
        >
          <ArrowRightLeft />
          Transfer
        </button>
      </div>

      <form className="money-form" onSubmit={handleSubmit}>
        <div className={action === 'transfer' ? 'money-field-grid transfer' : 'money-field-grid'}>
          {selected(action === 'transfer' ? 'From account' : 'Account', accountNumber, setAccountNumber)}
          {action === 'transfer' && selected('To account', toAccountNumber, setToAccountNumber)}
          <label>
            Amount
            <input
              min="0"
              onChange={(event) => setAmount(event.target.value)}
              placeholder="500"
              type="number"
              value={amount}
            />
          </label>
        </div>
        <button className="primary" disabled={accounts.length === 0} type="submit">
          {action === 'deposit' && <Banknote />}
          {action === 'withdraw' && <Wallet />}
          {action === 'transfer' && <ArrowRightLeft />}
          {actionLabel}
        </button>
      </form>
    </div>
  );
}

function PassTimeForm({ onSubmit }: { onSubmit: (command: string) => void }) {
  const [months, setMonths] = useState('1');

  return (
    <form
      className="inline-form"
      onSubmit={(event) => {
        event.preventDefault();
        onSubmit(`Pass ${months}`);
      }}
    >
      <input max="60" min="0" onChange={(event) => setMonths(event.target.value)} type="number" value={months} />
      <button aria-label="Pass time" title="Pass time" type="submit">
        <CalendarClock />
      </button>
    </form>
  );
}

function AccountCard({ account }: { account: Account }) {
  return (
    <article className="account-card">
      <div className="account-top">
        <span>{account.type}</span>
        <strong>{account.accountNumber}</strong>
      </div>
      <div className="balance">{formatMoney(account.balance)}</div>
      <dl>
        <div>
          <dt>APR</dt>
          <dd>{account.apr.toFixed(2)}%</dd>
        </div>
        {typeof account.monthsPassed === 'number' && (
          <div>
            <dt>Months</dt>
            <dd>{account.monthsPassed}</dd>
          </div>
        )}
        {typeof account.withdrawnThisMonth === 'boolean' && (
          <div>
            <dt>Withdrawn</dt>
            <dd>{account.withdrawnThisMonth ? 'Yes' : 'No'}</dd>
          </div>
        )}
      </dl>
      {account.transactionHistory.length > 0 && (
        <ul>
          {account.transactionHistory.slice(-3).map((transaction, index) => (
            <li key={`${transaction}-${index}`}>{transaction}</li>
          ))}
        </ul>
      )}
    </article>
  );
}

function CommandList({
  results,
  invalidCommands
}: {
  results: CommandResult[];
  invalidCommands: string[];
}) {
  if (results.length === 0 && invalidCommands.length === 0) {
    return <div className="empty-state compact">Commands appear here after you submit them.</div>;
  }

  return (
    <div className="command-list">
      {results.map((result, index) => (
        <div className={result.valid ? 'command valid' : 'command invalid'} key={`${result.command}-${index}`}>
          <span>{result.valid ? 'Accepted' : 'Rejected'}</span>
          <p>{result.command}</p>
        </div>
      ))}
      {invalidCommands.length > 0 && (
        <div className="invalid-box">
          <strong>Invalid command storage</strong>
          {invalidCommands.map((command, index) => (
            <p key={`${command}-${index}`}>{command}</p>
          ))}
        </div>
      )}
    </div>
  );
}

function formatMoney(value: number) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(value);
}

createRoot(document.getElementById('root')!).render(<App />);
