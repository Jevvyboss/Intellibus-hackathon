import React from 'react';
import { jamaicanResources } from '../data/resources';

const ResourceList: React.FC = () => {
  const categories: string[] = Object.keys(jamaicanResources);

  return (
    <div className="resource-list">
      <div className="resource-header">
        <h2>Mental Health Resources in Jamaica</h2>
        <p>Here are some trusted resources that can provide support and assistance</p>
      </div>

      {categories.map(category => (
        <div key={category} className="resource-category">
          <h3>{category}</h3>
          <div className="resources-grid">
            {jamaicanResources[category].map((resource, index) => (
              <div key={index} className="resource-card">
                <h4>{resource.name}</h4>
                <p>{resource.description}</p>
                <div className="resource-contact">
                  {resource.phone && (
                    <p><strong>Phone:</strong> {resource.phone}</p>
                  )}
                  {resource.website && (
                    <p>
                      <strong>Website:</strong>{' '}
                      <a href={resource.website} target="_blank" rel="noopener noreferrer">
                        Visit Website
                      </a>
                    </p>
                  )}
                  {resource.email && (
                    <p><strong>Email:</strong> {resource.email}</p>
                  )}
                  {resource.location && (
                    <p><strong>Location:</strong> {resource.location}</p>
                  )}
                </div>
                {resource.hours && (
                  <p className="resource-hours"><strong>Hours:</strong> {resource.hours}</p>
                )}
              </div>
            ))}
          </div>
        </div>
      ))}

      <div className="emergency-section">
        <h3>🚨 Emergency Contacts</h3>
        <div className="emergency-contacts">
          <div className="emergency-contact">
            <h4>Jamaica Mental Health Crisis Line</h4>
            <p className="emergency-phone">📞 888-NEW-LIFE (639-5433)</p>
            <p>24/7 Crisis Support</p>
          </div>
          <div className="emergency-contact">
            <h4>Emergency Services</h4>
            <p className="emergency-phone">📞 119</p>
            <p>Police, Fire, Ambulance</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ResourceList;