import { Component } from '@angular/core';
import { StoryService } from '../services/story.service';
import { UserStory } from '../models/user-story.model';

@Component({
  selector: 'app-user-story-generator',
  templateUrl: './user-story-generator.component.html',
  styleUrls: ['./user-story-generator.component.css'] // Can be empty if no specific styles
})
export class UserStoryGeneratorComponent {
  useCaseInput: string = '';
  generatedStories: UserStory[] = [];
  loading: boolean = false;
  simulatingJira: boolean = false;
  simulatedJiraSuccess: boolean = false;

  // Inline SVGs for icons (for use in template)
  Lightbulb = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-lightbulb"><path d="M15 14c.2-1 .7-1.7 1.5-2.5 1-.9 1.5-2.2 1.5-3.5A6 6 0 0 0 6 6c0 1.3.5 2.6 1.5 3.5.8.7 1.3 1.5 1.5 2.5"/><path d="M9 18h6"/><path d="M10 22v-2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1Z"/></svg>`;
  Workflow = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-workflow"><path d="M22 18H2v-6h20v6Z"/><path d="M22 12V6H2v6"/><path d="M12 6v6h-3"/><path d="M9 12l3 3 3-3"/></svg>`;
  CheckCircle = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-check-circle"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>`;
  XCircle = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-x-circle"><circle cx="12" cy="12" r="10"/><path d="m15 9-6 6"/><path d="m9 9 6 6"/></svg>`;
  JiraIcon = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor" stroke="none" class=""><path d="M16.5 12a4.5 4.5 0 0 1-9 0c0-2.485 2.015-4.5 4.5-4.5S16.5 9.515 16.5 12zM2 12h3M19 12h3M12 2v3M12 19v3M6.343 6.343l-2.121-2.121M17.657 17.657l2.121 2.121M6.343 17.657l-2.121 2.121M17.657 6.343l2.121-2.121"/></svg>`; // Simplified Jira-like icon

  constructor(private storyService: StoryService) { }

  handleGenerateStories(): void {
    if (!this.useCaseInput.trim()) {
      alert('Please enter a business use case.');
      return;
    }
    this.loading = true;
    this.simulatedJiraSuccess = false; // Reset Jira simulation status

    this.storyService.generateUserStories(this.useCaseInput).subscribe({
      next: (stories) => {
        this.generatedStories = stories;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error generating stories:', err);
        alert('Failed to generate stories. Please try again.');
        this.loading = false;
      }
    });
  }

  handleSimulateJira(): void {
    this.simulatingJira = true;
    this.storyService.simulateJiraCreation().subscribe({
      next: () => {
        this.simulatedJiraSuccess = true;
        this.simulatingJira = false;
      },
      error: (err) => {
        console.error('Error simulating Jira:', err);
        alert('Failed to simulate Jira creation.');
        this.simulatingJira = false;
      }
    });
  }
}